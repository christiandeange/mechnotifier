package com.deange.mechnotifier.topics

import android.util.Log
import com.deange.mechnotifier.dagger.Scope
import com.deange.mechnotifier.dagger.Scoped
import com.deange.mechnotifier.dagger.subscribeWithScope
import com.deange.mechnotifier.firebase.FirebaseTopics
import io.reactivex.Observable.just
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
    topicRepository.topics().subscribeWithScope(scope) { topics ->
      Log.d(TAG, "Topics: $topics")
    }

    topicRepository.topicChanges()
      .doOnNext { topicChange ->
        val deleted: Set<Topic> = topicChange.deleted
        val added: Set<Topic> = topicChange.added
        Log.d(TAG, "Topic diff: (deleted=$deleted, added=$added)")
      }
      .flatMap { topicChange ->
        firebaseTopics.applyChangesFrom(topicChange).andThen(just(topicChange))
      }
      .subscribeWithScope(scope) {
        Log.d(TAG, "Topic subscriptions diff applied.")
      }
  }

  private companion object {
    private val TAG = TopicWatcher::class.java.simpleName
  }
}
