package com.deange.mechnotifier.settings

import com.squareup.workflow1.StatelessWorkflow
import javax.inject.Inject

class SettingsWorkflow
@Inject constructor(
) : StatelessWorkflow<Unit, Unit, SettingsScreen>() {
  override fun render(
    props: Unit,
    context: RenderContext
  ): SettingsScreen {
    return SettingsScreen
  }
}
