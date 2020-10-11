package com.deange.mechnotifier.dagger

import com.deange.mechnotifier.MainActivity
import dagger.Component

@SingleInApp
@Component(modules = [AppModule::class])
interface AppComponent {
  fun inject(activity: MainActivity)
}
