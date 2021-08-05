package com.deange.mechnotifier.settings

import com.deange.mechnotifier.model.PostType
import com.deange.mechnotifier.topics.PublicType
import com.deange.mechnotifier.view.Text

class SettingsScreen(
  val region: Region,
  val subregion: Subregion?,
  val selectedPublicTypes: List<PublicType>,
  val selectedPostTypes: List<PostType>,
  val customRegionError: Text?,
  val onRegionPicked: (Region, Subregion?) -> Unit,
  val onPublicTypesChanged: (List<PublicType>) -> Unit,
  val onPostTypesChanged: (List<PostType>) -> Unit,
  val onSaveClicked: () -> Unit,
  val onBackClicked: () -> Unit
)
