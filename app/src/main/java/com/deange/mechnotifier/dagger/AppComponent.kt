package com.deange.mechnotifier.dagger

import com.deange.mechnotifier.MainActivity
import com.deange.mechnotifier.MainApplication
import dagger.Component

@SingleInApp
@Component(
    modules = [
      ApplicationModule::class,
      AppModule::class
    ]
)
interface AppComponent {
  fun inject(application: MainApplication)

  fun inject(activity: MainActivity)
}
