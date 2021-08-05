package com.deange.mechnotifier.settings

import com.deange.mechnotifier.model.PostType
import com.deange.mechnotifier.topics.PublicType
import com.deange.mechnotifier.view.Text

data class SettingsState(
  val region: Region,
  val subregion: Subregion?,
  val publicTypes: List<PublicType>,
  val postTypes: List<PostType>,
  val customRegionError: Text? = null
)
