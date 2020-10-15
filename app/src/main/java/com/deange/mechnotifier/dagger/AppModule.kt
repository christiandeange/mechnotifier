package com.deange.mechnotifier.dagger

import com.deange.mechnotifier.notification.NotificationChannels
import com.deange.mechnotifier.topics.TopicWatcher
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet

@Module
abstract class AppModule {

  @Binds @IntoSet @SingleInApp
  abstract fun bindsTopicWatcherAsScoped(
    topicWatcher: TopicWatcher
  ): Scoped

  @Binds @IntoSet @SingleInApp
  abstract fun bindsNotificationChannelsAsScoped(
    notificationChannels: NotificationChannels
  ): Scoped

  companion object {
    @Provides @ElementsIntoSet @SingleInApp
    fun providesScoped(): Set<@JvmSuppressWildcards Scoped> = emptySet()

    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder().build()
  }
}
