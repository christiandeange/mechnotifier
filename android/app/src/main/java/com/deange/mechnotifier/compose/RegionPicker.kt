package com.deange.mechnotifier.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deange.mechnotifier.R
import com.deange.mechnotifier.settings.AnyRegion
import com.deange.mechnotifier.settings.CanadaRegion
import com.deange.mechnotifier.settings.EuropeRegion
import com.deange.mechnotifier.settings.NoRegion
import com.deange.mechnotifier.settings.OtherRegion
import com.deange.mechnotifier.settings.Region
import com.deange.mechnotifier.settings.Subregion
import com.deange.mechnotifier.settings.UsaRegion
import com.deange.mechnotifier.view.Text

@Composable
fun RowScope.RegionPicker(
  regions: List<Region>,
  selectedRegion: Region,
  selectedSubregion: Subregion?,
  customRegionError: Text?,
  onRegionSelected: (Region, Subregion?) -> Unit
) {
  val showRegionPicker = remember { mutableStateOf(false) }

  Column(modifier = Modifier.alignByBaseline().animateContentSize()) {
    Button(
      modifier = Modifier.fillMaxWidth(),
      onClick = { showRegionPicker.value = true }
    ) {
      Text(text = stringResource(selectedRegion.nameResId))
    }
    DropdownMenu(
      expanded = showRegionPicker.value,
      onDismissRequest = { showRegionPicker.value = false },
    ) {
      regions.forEach { region ->
        DropdownMenuItem(
          modifier = Modifier.fillMaxWidth(),
          onClick = {
            showRegionPicker.value = false
            onRegionSelected(region, region.subregions.firstOrNull())
          }
        ) {
          Text(text = stringResource(region.nameResId))
        }
      }
    }

    when (selectedRegion) {
      is AnyRegion,
      is NoRegion -> Unit

      is CanadaRegion,
      is EuropeRegion,
      is UsaRegion -> {
        SubregionPicker(
          region = selectedRegion,
          selectedSubregion = selectedSubregion,
          onSubregionSelected = { subregion ->
            onRegionSelected(selectedRegion, subregion)
          }
        )
      }

      is OtherRegion -> {
        RegionEditor(
          selectedRegion = selectedRegion,
          errorMessage = customRegionError?.evaluate()?.toString(),
          onRegionSelected = onRegionSelected
        )
      }
    }
  }
}

@Composable
private fun SubregionPicker(
  region: Region,
  selectedSubregion: Subregion?,
  onSubregionSelected: (Subregion) -> Unit
) {
  val showSubregionPicker = remember { mutableStateOf(false) }

  Spacer(modifier = Modifier.height(8.dp))
  Button(
    modifier = Modifier.fillMaxWidth(),
    onClick = { showSubregionPicker.value = true }
  ) {
    Text(text = selectedSubregion?.nameResId?.let { stringResource(it) } ?: "")
  }
  DropdownMenu(
    expanded = showSubregionPicker.value,
    onDismissRequest = { showSubregionPicker.value = false },
  ) {
    region.subregions.forEach { subregion ->
      DropdownMenuItem(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
          showSubregionPicker.value = false
          onSubregionSelected(subregion)
        }
      ) {
        Text(text = stringResource(subregion.nameResId))
      }
    }
  }
}

@Composable
private fun RegionEditor(
  selectedRegion: Region,
  errorMessage: String?,
  onRegionSelected: (Region, Subregion?) -> Unit
) {
  Spacer(modifier = Modifier.height(8.dp))
  OutlinedTextField(
    modifier = Modifier.fillMaxWidth(),
    value = selectedRegion.regionCode,
    onValueChange = { onRegionSelected(OtherRegion(it.uppercase().take(2)), null) },
    isError = errorMessage != null,
    placeholder = {
      Text(stringResource(R.string.region_editor_country_code))
    },
    label = {
      errorMessage?.let { Text(it) }
    }
  )
}

val BaseRegions = listOf(NoRegion, AnyRegion, CanadaRegion, EuropeRegion, UsaRegion)
