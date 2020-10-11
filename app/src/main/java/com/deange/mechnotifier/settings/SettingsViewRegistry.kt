package com.deange.mechnotifier.settings

import com.squareup.workflow1.ui.ViewRegistry
import javax.inject.Inject

class SettingsViewRegistry
@Inject constructor() : ViewRegistry
by ViewRegistry(SettingsLayoutRunner)
