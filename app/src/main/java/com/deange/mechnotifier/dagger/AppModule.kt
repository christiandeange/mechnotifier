package com.deange.mechnotifier.dagger

import android.app.Application
import com.deange.mechnotifier.dagger.SingleInApp
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {

  @Provides
  @SingleInApp
  fun provideApplication(): Application {
    return application
  }
}
