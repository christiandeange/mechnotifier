package com.deange.mechnotifier.topics

import android.util.Log
import com.deange.mechnotifier.dagger.Scope
import com.deange.mechnotifier.dagger.Scoped
import com.deange.mechnotifier.firebase.FirebaseTopics
import kotlinx.coroutines.flow.collect
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
    scope.launch {
      topicRepository.topics().collect { topics ->
        Log.d(TAG, "Topics: $topics")
      }
    }

    scope.launch {
      topicRepository.topicChanges().collect { topicChange ->
        val deleted: Set<Topic> = topicChange.deleted
        val added: Set<Topic> = topicChange.added
        Log.d(TAG, "Topic diff: (deleted=$deleted, added=$added)")

        firebaseTopics.applyChangesFrom(topicChange)
        Log.d(TAG, "Topic subscriptions diff applied.")
      }
    }
  }

  private companion object {
    private val TAG = TopicWatcher::class.java.simpleName
  }
}
