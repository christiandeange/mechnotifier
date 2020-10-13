package com.deange.mechnotifier.topics

import android.util.Log
import com.deange.mechnotifier.dagger.Scope
import com.deange.mechnotifier.dagger.Scoped
import com.deange.mechnotifier.dagger.subscribeWithScope
import javax.inject.Inject

/**
 * Monitors any changes in watched topics. Used for debugging purposes only.
 */
class TopicWatcher
@Inject constructor(
  private val topicRepository: TopicRepository
) : Scoped {
  override fun onEnterScope(scope: Scope) {
    topicRepository.topics().subscribeWithScope(scope) { topics ->
      Log.d("TopicWatcher", "Topics: ${topics.asStrings()}")
    }

    topicRepository.topicChanges().subscribeWithScope(scope) { topicChange ->
      val deleted = topicChange.deleted.asStrings()
      val added = topicChange.added.asStrings()
      Log.d("TopicWatcher", "Topics changed: (deleted=$deleted, added=$added)")
    }
  }
}
