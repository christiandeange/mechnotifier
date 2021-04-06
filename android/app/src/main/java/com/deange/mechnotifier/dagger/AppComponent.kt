package com.deange.mechnotifier.dagger

import com.deange.mechnotifier.MainActivity
import com.deange.mechnotifier.MainApplication
import com.deange.mechnotifier.firebase.FirebaseTopicNotificationService
import com.deange.mechnotifier.model.PostSerializer
import com.deange.mechnotifier.notification.NotificationActionReceiver
import dagger.Component

@SingleInApp
@Component(
  modules = [
    ApplicationModule::class,
    AppModule::class
  ]
)
interface AppComponent {
  fun modelCreator(): PostSerializer

  fun inject(application: MainApplication)

  fun inject(activity: MainActivity)

  fun inject(service: FirebaseTopicNotificationService)

  fun inject(receiver: NotificationActionReceiver)
}
