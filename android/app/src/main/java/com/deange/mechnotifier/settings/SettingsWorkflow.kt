package com.deange.mechnotifier.settings

import com.deange.mechnotifier.R
import com.deange.mechnotifier.model.PostFilter
import com.deange.mechnotifier.model.PostType
import com.deange.mechnotifier.topics.PublicType
import com.deange.mechnotifier.topics.Topic
import com.deange.mechnotifier.topics.TopicCreator
import com.deange.mechnotifier.topics.TopicRepository
import com.deange.mechnotifier.view.Text
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction
import javax.inject.Inject

private typealias Action =
    WorkflowAction<SettingsWorkflowProps, SettingsState, Unit>.Updater

class SettingsWorkflow
@Inject constructor(
  private val topicCreator: TopicCreator,
  private val topicRepository: TopicRepository
) : StatefulWorkflow<SettingsWorkflowProps, SettingsState, Unit, SettingsScreen>() {
  override fun initialState(
    props: SettingsWorkflowProps,
    snapshot: Snapshot?
  ): SettingsState = SettingsState(
    region = props.region,
    subregion = props.subregion,
    publicTypes = props.publicTypes,
    postTypes = props.postFilter.postTypes,
    customRegionError = null
  )

  override fun render(
    renderProps: SettingsWorkflowProps,
    renderState: SettingsState,
    context: RenderContext
  ): SettingsScreen {
    return SettingsScreen(
      region = renderState.region,
      subregion = renderState.subregion,
      selectedPublicTypes = renderState.publicTypes,
      selectedPostTypes = renderState.postTypes,
      customRegionError = renderState.customRegionError,
      onRegionPicked = context.eventHandler { region, subregion ->
        onRegionPicked(region, subregion)
      },
      onPublicTypesChanged = context.eventHandler { publicTypes ->
        onPublicTypesChanged(publicTypes)
      },
      onPostTypesChanged = context.eventHandler { postTypes ->
        onPostTypesChanged(postTypes)
      },
      onSaveClicked = context.eventHandler { onSaveClicked() },
      onBackClicked = context.eventHandler { onBackClicked() }
    )
  }

  override fun snapshotState(state: SettingsState): Snapshot? = null

  private fun Action.onRegionPicked(
    region: Region,
    subregion: Subregion?
  ) {
    state = state.copy(
      region = region,
      subregion = subregion,
      customRegionError = null
    )
  }

  private fun Action.onPublicTypesChanged(publicTypes: List<PublicType>) {
    state = state.copy(publicTypes = publicTypes)
  }

  private fun Action.onPostTypesChanged(postTypes: List<PostType>) {
    state = state.copy(postTypes = postTypes)
  }

  private fun Action.onSaveClicked() {
    val region: Region = state.region
    val regionCode: String = state.region.regionCode

    state = state.copy(
      customRegionError = when {
        region !is OtherRegion -> null
        !regionCode.matches(Regex("[A-Z]{2}")) -> Text(R.string.region_other_error)
        regionCode in PublicType.tagNames() -> Text(R.string.region_reserved_code)
        else -> null
      }
    )

    if (state.customRegionError == null) {
      val topics = mutableSetOf<Topic>()
      topics += topicCreator.fromRegion(state.region, state.subregion)
      topics += state.publicTypes.map { topicCreator.fromPublicType(it) }

      val postFilter = PostFilter(state.postTypes)

      topicRepository.setTopics(topics.toSet())
      topicRepository.setPostFilter(postFilter)
    }
  }

  private fun Action.onBackClicked() {
    setOutput(Unit)
  }
}
