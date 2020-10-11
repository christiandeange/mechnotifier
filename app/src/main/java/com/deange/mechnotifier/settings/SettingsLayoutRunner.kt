package com.deange.mechnotifier.settings

import android.view.View
import com.deange.mechnotifier.R.layout
import com.squareup.workflow1.ui.LayoutRunner
import com.squareup.workflow1.ui.LayoutRunner.Companion.bind
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.ViewFactory

class SettingsLayoutRunner(view: View) : LayoutRunner<SettingsScreen> {
  override fun showRendering(
    rendering: SettingsScreen,
    viewEnvironment: ViewEnvironment
  ) = Unit

  companion object : ViewFactory<SettingsScreen> by bind(
      layout.settings_screen,
      ::SettingsLayoutRunner
  )
}
