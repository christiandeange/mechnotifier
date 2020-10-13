package com.deange.mechnotifier.dagger

import com.deange.mechnotifier.topics.TopicWatcher
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

  companion object {
    @Provides @ElementsIntoSet @SingleInApp
    fun bindsScoped(): Set<@JvmSuppressWildcards Scoped> = emptySet()
  }
}
