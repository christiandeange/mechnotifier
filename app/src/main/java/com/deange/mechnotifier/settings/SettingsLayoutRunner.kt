package com.deange.mechnotifier.settings

import android.view.View
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.deange.mechnotifier.R
import com.deange.mechnotifier.R.layout
import com.deange.mechnotifier.view.NameableAdapter
import com.deange.mechnotifier.view.SimpleItemSelectedListener
import com.squareup.workflow1.ui.LayoutRunner
import com.squareup.workflow1.ui.LayoutRunner.Companion.bind
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.ViewFactory
import com.squareup.workflow1.ui.backPressedHandler

class SettingsLayoutRunner(private val view: View) : LayoutRunner<SettingsScreen> {

  private val toolbar: Toolbar = view.findViewById(R.id.toolbar)
  private val regionPicker: Spinner = view.findViewById(R.id.region_picker)
  private val regionEditor: EditText = view.findViewById(R.id.region_editor)
  private val subregionPicker: Spinner = view.findViewById(R.id.subregion_picker)

  private val baseRegions = listOf(AnyRegion, CanadaRegion, EuropeRegion, UsaRegion)

  private val regionPickerAdapter = NameableAdapter<Region>(regionPicker.context)
  private val subregionPickerAdapter = NameableAdapter<Subregion>(subregionPicker.context)

  init {
    toolbar.inflateMenu(R.menu.settings_menu)
    toolbar.navigationIcon = getDrawable(toolbar.context, R.drawable.abc_ic_ab_back_material)

    regionPicker.adapter = regionPickerAdapter
    subregionPicker.adapter = subregionPickerAdapter
  }

  override fun showRendering(
    rendering: SettingsScreen,
    viewEnvironment: ViewEnvironment
  ) {
    view.backPressedHandler = rendering.onBackClicked

    toolbar.setNavigationOnClickListener { rendering.onBackClicked() }
    toolbar.setOnMenuItemClickListener { item ->
      when (item.itemId) {
        R.id.menu_save -> {
          rendering.onSaveClicked()
          true
        }
        else -> false
      }
    }

    val regions = baseRegions + if (rendering.region is OtherRegion) {
      rendering.region
    } else {
      OtherRegion(regionCode = "")
    }

    regionPicker.onItemSelectedListener = null
    regionPickerAdapter.items = regions
    regionPicker.setSelection(regions.indexOf(rendering.region))
    regionPicker.onItemSelectedListener = RegionPickerListener(rendering)

    regionEditor.doAfterTextChanged { text ->
      if (regionEditor.isVisible) {
        rendering.onRegionPicked(OtherRegion(regionCode = text.toString()), null)
      }
    }
    regionEditor.error = rendering.customRegionError?.evaluate(regionEditor.context)
    if (regionEditor.error != null) {
      regionEditor.requestFocus()
    }

    val region = rendering.region
    val subregion = rendering.subregion

    subregionPicker.onItemSelectedListener = null
    subregionPicker.isVisible = region.subregions.isNotEmpty()
    subregionPickerAdapter.items = region.subregions
    subregionPicker.setSelection(region.subregions.indexOf(subregion))
    subregionPicker.onItemSelectedListener = SubregionPickerListener(rendering)

    regionEditor.isVisible = region is OtherRegion
  }

  private inner class RegionPickerListener(
    private val rendering: SettingsScreen,
  ) : SimpleItemSelectedListener() {
    override fun onItemSelected(position: Int) {
      val region = regionPickerAdapter.getItem(position)
      rendering.onRegionPicked(region, region.subregions.firstOrNull())
    }
  }

  private inner class SubregionPickerListener(
    private val rendering: SettingsScreen
  ) : SimpleItemSelectedListener() {
    override fun onItemSelected(position: Int) {
      rendering.onRegionPicked(rendering.region, subregionPickerAdapter.getItem(position))
    }
  }

  companion object : ViewFactory<SettingsScreen> by bind(
      layout.settings_screen,
      ::SettingsLayoutRunner
  )
}
