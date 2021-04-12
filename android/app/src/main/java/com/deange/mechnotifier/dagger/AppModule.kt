package com.deange.mechnotifier.dagger

import com.deange.mechnotifier.model.PostFilter
import com.deange.mechnotifier.model.PostFilterSerializer
import com.deange.mechnotifier.notification.NotificationChannels
import com.deange.mechnotifier.topics.TopicRepository
import com.deange.mechnotifier.topics.TopicWatcher
import com.squareup.moshi.Moshi
import com.tfcporciuncula.flow.Serializer
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
  abstract fun bindsTopicRepositoryAsScoped(
    topicRepository: TopicRepository
  ): Scoped

  @Binds @IntoSet @SingleInApp
  abstract fun bindsNotificationChannelsAsScoped(
    notificationChannels: NotificationChannels
  ): Scoped

  @Binds
  abstract fun bindsPostFilterSerializer(
    converter: PostFilterSerializer
  ): Serializer<PostFilter>

  companion object {
    @Provides @ElementsIntoSet @SingleInApp
    fun providesScoped(): Set<@JvmSuppressWildcards Scoped> = emptySet()

    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder().build()
  }
}
