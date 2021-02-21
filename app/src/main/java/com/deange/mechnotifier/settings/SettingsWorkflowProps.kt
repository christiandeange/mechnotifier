package com.deange.mechnotifier.settings

import com.deange.mechnotifier.model.PostFilter
import com.deange.mechnotifier.topics.PublicType

data class SettingsWorkflowProps(
  val region: Region,
  val subregion: Subregion?,
  val publicTypes: Set<PublicType>,
  val postFilter: PostFilter
)
