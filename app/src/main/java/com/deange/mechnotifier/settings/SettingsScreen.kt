package com.deange.mechnotifier.settings

class SettingsScreen(
  val region: Region,
  val subregion: Subregion?,
  val onRegionPicked: (Region, Subregion?) -> Unit
)
