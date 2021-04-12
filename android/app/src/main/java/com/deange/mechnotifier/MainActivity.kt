package com.deange.mechnotifier

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.deange.mechnotifier.dagger.Scope
import com.deange.mechnotifier.model.PostFilter
import com.deange.mechnotifier.settings.SettingsViewRegistry
import com.deange.mechnotifier.settings.SettingsWorkflow
import com.deange.mechnotifier.settings.SettingsWorkflowProps
import com.deange.mechnotifier.topics.Topic
import com.deange.mechnotifier.topics.TopicCreator
import com.deange.mechnotifier.topics.TopicRepository
import com.squareup.workflow1.ui.WorkflowRunner
import com.squareup.workflow1.ui.setContentWorkflow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject lateinit var settingsViewRegistry: SettingsViewRegistry
  @Inject lateinit var settingsWorkflow: SettingsWorkflow
  @Inject lateinit var topicRepository: TopicRepository
  @Inject lateinit var topicCreator: TopicCreator

  private lateinit var scope: Scope

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mainApplication.appComponent.inject(this)
    scope = mainApplication.scope + this::class.java.name

    scope.launch {
      val topics = topicRepository.topics().first()
      val filter = topicRepository.postFilter().first()

      withContext(Dispatchers.Main) {
        startWorkflow(topics, filter)
      }
    }
  }

  private fun startWorkflow(
    topics: Set<Topic>,
    postFilter: PostFilter
  ) {
    val (region, subregion) = topics.mapNotNull { topicCreator.toRegionOrNull(it) }.first()
    val publicTypes = topics.mapNotNull { topicCreator.toPublicTypeOrNull(it) }.toSet()

    setContentWorkflow(
      registry = settingsViewRegistry,
      configure = {
        WorkflowRunner.Config(
          workflow = settingsWorkflow,
          props = SettingsWorkflowProps(
            region = region,
            subregion = subregion,
            publicTypes = publicTypes,
            postFilter = postFilter
          )
        )
      },
      onResult = { finishAfterTransition() }
    )
  }

  override fun onDestroy() {
    scope.destroy()
    super.onDestroy()
  }
}
