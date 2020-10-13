package com.deange.mechnotifier.settings

import com.deange.mechnotifier.view.Text

data class SettingsState(
  val region: Region,
  val subregion: Subregion?,
  val customRegionError: Text? = null
)
