package com.deange.mechnotifier.topics

import android.util.Log
import com.deange.mechnotifier.dagger.Scope
import com.deange.mechnotifier.dagger.Scoped
import com.deange.mechnotifier.dagger.collectIn
import com.deange.mechnotifier.firebase.FirebaseTopics
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Monitors any changes in watched topics.
 */
class TopicWatcher
@Inject constructor(
  private val topicRepository: TopicRepository,
  private val firebaseTopics: FirebaseTopics
) : Scoped {
  override fun onEnterScope(scope: Scope) {
    topicRepository.topics().collectIn(scope) { topics ->
      Log.d(TAG, "Topics: $topics")
    }

    topicRepository.topicChanges()
      .onEach { topicChange ->
        val deleted: Set<Topic> = topicChange.deleted
        val added: Set<Topic> = topicChange.added
        Log.d(TAG, "Topic diff: (deleted=$deleted, added=$added)")
      }
      .flatMapLatest { topicChange ->
        firebaseTopics.applyChangesFrom(topicChange)
        flowOf(topicChange)
      }
      .collectIn(scope) {
        Log.d(TAG, "Topic subscriptions diff applied.")
      }
  }

  private companion object {
    private val TAG = TopicWatcher::class.java.simpleName
  }
}
