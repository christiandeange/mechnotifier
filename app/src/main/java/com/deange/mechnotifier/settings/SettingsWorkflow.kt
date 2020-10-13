package com.deange.mechnotifier.settings

import com.deange.mechnotifier.R
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
) : StatefulWorkflow<Unit, SettingsState, Unit, SettingsScreen>() {
  override fun initialState(
    props: Unit,
    snapshot: Snapshot?
  ): SettingsState = SettingsState(region = AnyRegion, subregion = null)

  override fun render(
    props: Unit,
    state: SettingsState,
    context: RenderContext
  ): SettingsScreen {
    return SettingsScreen(
        state.region,
        state.subregion,
        state.customRegionError,
        onRegionPicked = { region, subregion ->
          context.actionSink.send(onRegionPicked(region, subregion))
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

  private fun onSaveClicked() = action {
    if (state.region is OtherRegion && !state.region.regionCode.matches(Regex("\\s\\s"))) {
      state = state.copy(customRegionError = Text(R.string.region_other_error))
    } else {
      val topic: Topic = topicCreator.fromRegion(state.region, state.subregion)
      topicRepository.setTopics(setOf(topic))
    }
  }

  private fun onBackClicked() = action {
    setOutput(Unit)
  }
}
