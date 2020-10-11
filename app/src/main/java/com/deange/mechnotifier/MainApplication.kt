package com.deange.mechnotifier

import android.app.Application
import com.deange.mechnotifier.dagger.AppComponent
import com.deange.mechnotifier.dagger.AppModule
import com.deange.mechnotifier.dagger.DaggerAppComponent

class MainApplication : Application() {
  val appComponent: AppComponent by lazy {
    DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .build()
  }
}
