package com.deange.mechnotifier.settings

import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.deange.mechnotifier.R
import com.deange.mechnotifier.model.PostType
import com.deange.mechnotifier.topics.PublicType
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
  private val postFilterGroup: RadioGroup = view.findViewById(R.id.post_type_group)
  private val publicFilterGroup: RadioGroup = view.findViewById(R.id.public_type_group)
  private val postFilterWarning: View = view.findViewById(R.id.post_type_empty_warning)

  private val baseRegions = listOf(NoRegion, AnyRegion, CanadaRegion, EuropeRegion, UsaRegion)

  private val regionPickerAdapter = NameableAdapter<Region>(regionPicker.context)
  private val subregionPickerAdapter = NameableAdapter<Subregion>(subregionPicker.context)

  init {
    toolbar.inflateMenu(R.menu.settings_menu)
    toolbar.setTitle(R.string.app_name)

    regionPicker.adapter = regionPickerAdapter
    subregionPicker.adapter = subregionPickerAdapter
  }

  override fun showRendering(
    rendering: SettingsScreen,
    viewEnvironment: ViewEnvironment
  ) {
    view.backPressedHandler = rendering.onBackClicked

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

    postFilterGroup.children.forEach { child ->
      val checkbox = child as CheckBox

      checkbox.setOnCheckedChangeListener(null)
      checkbox.isEnabled = region !is NoRegion
      checkbox.isChecked = checkbox.isEnabled && checkbox.postType in rendering.selectedPostTypes
      checkbox.setOnCheckedChangeListener { _, isChecked ->
        val newTypes = if (isChecked) {
          rendering.selectedPostTypes + checkbox.postType
        } else {
          rendering.selectedPostTypes - checkbox.postType
        }.distinct()

        rendering.onPostTypesChanged(newTypes)
      }
    }
    postFilterWarning.isGone = region is NoRegion || rendering.selectedPostTypes.isNotEmpty()

    publicFilterGroup.children.forEach { child ->
      val checkbox = child as CheckBox

      checkbox.setOnCheckedChangeListener(null)
      checkbox.isChecked = checkbox.publicType in rendering.selectedPublicTypes
      checkbox.setOnCheckedChangeListener { _, isChecked ->
        val newTypes = if (isChecked) {
          rendering.selectedPublicTypes + checkbox.publicType
        } else {
          rendering.selectedPublicTypes - checkbox.publicType
        }

        rendering.onPublicTypesChanged(newTypes.toSet())
      }
    }
  }

  private val CheckBox.postType: PostType
    get() = PostType.values()[(parent as ViewGroup).indexOfChild(this)]

  private val CheckBox.publicType: PublicType
    get() = PublicType.fromTagName(tag.toString())

  private inner class RegionPickerListener(
    private val rendering: SettingsScreen,
  ) : SimpleItemSelectedListener() {
    override fun onItemSelected(position: Int) {
      val region = regionPickerAdapter.getItem(position)
      if (region != rendering.region) {
        rendering.onRegionPicked(region, region.subregions.firstOrNull())
      }
    }
  }

  private inner class SubregionPickerListener(
    private val rendering: SettingsScreen
  ) : SimpleItemSelectedListener() {
    override fun onItemSelected(position: Int) {
      val subregion = subregionPickerAdapter.getItem(position)
      if (subregion != rendering.subregion) {
        rendering.onRegionPicked(rendering.region, subregion)
      }
    }
  }

  companion object : ViewFactory<SettingsScreen> by bind(
      R.layout.settings_screen,
      ::SettingsLayoutRunner
  )
}
