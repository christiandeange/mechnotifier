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
import com.squareup.workflow1.action
import javax.inject.Inject

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
    props: SettingsWorkflowProps,
    state: SettingsState,
    context: RenderContext
  ): SettingsScreen {
    return SettingsScreen(
        state.region,
        state.subregion,
        state.publicTypes,
        state.postTypes,
        state.customRegionError,
        onRegionPicked = { region, subregion ->
          context.actionSink.send(onRegionPicked(region, subregion))
        },
        onPublicTypesChanged = { publicTypes ->
          context.actionSink.send(onPublicTypesChanged(publicTypes))
        },
        onPostTypesChanged = { postTypes ->
          context.actionSink.send(onPostTypesChanged(postTypes))
        },
        onSaveClicked = { context.actionSink.send(onSaveClicked()) },
        onBackClicked = { context.actionSink.send(onBackClicked()) }
    )
  }

  override fun snapshotState(state: SettingsState): Snapshot? = null

  private fun onRegionPicked(
    region: Region,
    subregion: Subregion?
  ) = action {
    state = state.copy(
        region = region,
        subregion = subregion,
        customRegionError = null
    )
  }

  private fun onPublicTypesChanged(publicTypes: Set<PublicType>) = action {
    state = state.copy(publicTypes = publicTypes)
  }

  private fun onPostTypesChanged(postTypes: List<PostType>) = action {
    state = state.copy(postTypes = postTypes)
  }

  private fun onSaveClicked() = action {
    val region: Region = state.region
    val regionCode: String = state.region.regionCode

    state = state.copy(
        customRegionError = when {
          region !is OtherRegion -> null
          !regionCode.matches(Regex("\\s\\s")) -> Text(R.string.region_other_error)
          regionCode in PublicType.tagNames() -> Text(R.string.region_reserved_code)
          else -> null
        }
    )

    if (state.customRegionError == null) {
      val topics: Set<Topic> = buildSet {
        add(topicCreator.fromRegion(state.region, state.subregion))
        addAll(state.publicTypes.map { topicCreator.fromPublicType(it) })
      }

      val postFilter = PostFilter(state.postTypes)

      topicRepository.setTopics(topics)
      topicRepository.setPostFilter(postFilter)
    }
  }

  private fun onBackClicked() = action {
    setOutput(Unit)
  }
}
