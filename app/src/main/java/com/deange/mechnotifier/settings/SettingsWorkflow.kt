package com.deange.mechnotifier.settings

import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.action
import javax.inject.Inject

class SettingsWorkflow
@Inject constructor(
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
        onRegionPicked = { region, subregion ->
          context.actionSink.send(action {
            this.state = state.copy(region = region, subregion = subregion)
          })
        }
    )
  }

  override fun snapshotState(state: SettingsState): Snapshot? = null
}
