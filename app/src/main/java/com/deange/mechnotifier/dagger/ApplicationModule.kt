package com.deange.mechnotifier.dagger

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: Application) {

  @Provides
  @SingleInApp
  fun provideApplication(): Application {
    return application
  }
}
