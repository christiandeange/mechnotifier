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
      Log.d(TAG, "Topics: ${topics.asStrings()}")
    }

    topicRepository.topicChanges()
        .doOnNext { topicChange ->
          val deleted = topicChange.deleted.asStrings()
          val added = topicChange.added.asStrings()
          Log.d(TAG, "Topic diff: (deleted=$deleted, added=$added) [@${topicChange.hashCode()}]")
        }
        .flatMap { firebaseTopics.applyChangesFrom(it).andThen(just(it)) }
        .subscribeWithScope(scope) { topicChange ->
          Log.d(TAG, "Topic subscriptions diff applied [@${topicChange.hashCode()}]")
        }
  }

  private companion object {
    private val TAG = TopicWatcher::class.java.simpleName
  }
}
