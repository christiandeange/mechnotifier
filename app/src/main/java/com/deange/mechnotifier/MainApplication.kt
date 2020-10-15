package com.deange.mechnotifier

import android.app.Application
import android.content.Context
import com.deange.mechnotifier.dagger.AppComponent
import com.deange.mechnotifier.dagger.ApplicationModule
import com.deange.mechnotifier.dagger.DaggerAppComponent
import com.deange.mechnotifier.dagger.Scope
import com.deange.mechnotifier.dagger.Scoped
import com.deange.mechnotifier.dagger.SingleInApp
import javax.inject.Inject

class MainApplication : Application() {

  @Inject @SingleInApp lateinit var applicationScoped: Set<@JvmSuppressWildcards Scoped>

  val appComponent: AppComponent by lazy {
    DaggerAppComponent.builder()
        .applicationModule(ApplicationModule(this))
        .build()
  }

  val scope: Scope = Scope.ROOT

  override fun onCreate() {
    super.onCreate()

    appComponent.inject(this)

    // Application-scoped objects are not notified when the scope is destroyed; the app just exits.
    applicationScoped.forEach { it.onEnterScope(scope) }
  }
}

val Context.mainApplication: MainApplication
  get() = applicationContext as MainApplication
