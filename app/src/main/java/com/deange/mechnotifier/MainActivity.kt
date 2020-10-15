package com.deange.mechnotifier

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.deange.mechnotifier.settings.SettingsViewRegistry
import com.deange.mechnotifier.settings.SettingsWorkflow
import com.squareup.workflow1.ui.WorkflowRunner
import com.squareup.workflow1.ui.setContentWorkflow
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject lateinit var settingsViewRegistry: SettingsViewRegistry
  @Inject lateinit var settingsWorkflow: SettingsWorkflow

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mainApplication.appComponent.inject(this)

    setContentWorkflow(
        settingsViewRegistry,
        configure = { WorkflowRunner.Config(settingsWorkflow) },
        onResult = { finishAfterTransition() }
    )
  }
}
