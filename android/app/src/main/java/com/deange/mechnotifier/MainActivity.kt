package com.deange.mechnotifier

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.deange.mechnotifier.dagger.Scope
import com.deange.mechnotifier.model.PostFilter
import com.deange.mechnotifier.settings.SettingsScreen
import com.deange.mechnotifier.settings.SettingsViewRegistry
import com.deange.mechnotifier.settings.SettingsWorkflow
import com.deange.mechnotifier.settings.SettingsWorkflowProps
import com.deange.mechnotifier.topics.Topic
import com.deange.mechnotifier.topics.TopicCreator
import com.deange.mechnotifier.topics.TopicRepository
import com.squareup.workflow1.ui.WorkflowLayout
import com.squareup.workflow1.ui.renderWorkflowIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject lateinit var settingsViewRegistry: SettingsViewRegistry
  @Inject lateinit var settingsWorkflow: SettingsWorkflow
  @Inject lateinit var topicRepository: TopicRepository
  @Inject lateinit var topicCreator: TopicCreator

  private val viewModel: MainViewModel by viewModels {
    MainViewModelFactory(this, settingsWorkflow, intent.extras)
  }

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
    val props = SettingsWorkflowProps(
      region = region,
      subregion = subregion,
      publicTypes = publicTypes,
      postFilter = postFilter
    )

    setContentView(
      WorkflowLayout(this).apply {
        start(viewModel.render(props), settingsViewRegistry)
      }
    )

    lifecycleScope.launch {
      viewModel.waitForExit()
      finishAfterTransition()
    }
  }

  override fun onDestroy() {
    scope.destroy()
    super.onDestroy()
  }
}

class MainViewModelFactory(
  owner: SavedStateRegistryOwner,
  private val workflow: SettingsWorkflow,
  defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(
    key: String,
    modelClass: Class<T>,
    handle: SavedStateHandle
  ): T = MainViewModel(handle, workflow) as T
}

class MainViewModel(
  private val savedState: SavedStateHandle,
  private val workflow: SettingsWorkflow
) : ViewModel() {
  private val running = Job()

  fun render(props: SettingsWorkflowProps): Flow<SettingsScreen> {
    return renderWorkflowIn(
      workflow = workflow,
      prop = props,
      scope = viewModelScope,
      savedStateHandle = savedState,
      onOutput = { running.complete() }
    )
  }

  suspend fun waitForExit() = running.join()
}
