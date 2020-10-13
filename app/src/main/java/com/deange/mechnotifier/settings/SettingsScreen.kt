package com.deange.mechnotifier.settings

import com.deange.mechnotifier.view.Text

class SettingsScreen(
  val region: Region,
  val subregion: Subregion?,
  val customRegionError: Text?,
  val onRegionPicked: (Region, Subregion?) -> Unit,
  val onSaveClicked: () -> Unit,
  val onBackClicked: () -> Unit
)
