package com.deange.mechnotifier.firebase

import android.util.Log
import com.deange.mechnotifier.topics.Topic
import com.deange.mechnotifier.topics.TopicChange
import com.deange.mechnotifier.topics.asStrings
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class FirebaseTopics
@Inject constructor(
) {
  private val firebase: FirebaseMessaging = FirebaseMessaging.getInstance()

  suspend fun unsubscribeFrom(topic: Topic) {
    unsubscribeFrom(setOf(topic))
  }

  suspend fun unsubscribeFrom(topics: Set<Topic>) {
    val unsubscribeTasks: List<Task<Void>> = topics.map { topic ->
      firebase.unsubscribeFromTopic(topic.name)
    }

    Tasks.whenAll(unsubscribeTasks).asFlow().collect()
  }

  suspend fun subscribeTo(topic: Topic) {
    subscribeTo(setOf(topic))
  }

  suspend fun subscribeTo(topics: Set<Topic>) {
    val subscribeTasks: List<Task<Void>> = topics.map { topic ->
      firebase.subscribeToTopic(topic.name)
    }

    Tasks.whenAll(subscribeTasks).asFlow().collect()
  }

  suspend fun applyChangesFrom(topicChange: TopicChange) {
    coroutineScope {
      listOf(
        async {
          Log.d(TAG, "Unsubscribe from ${topicChange.deleted.asStrings()}")
          unsubscribeFrom(topicChange.deleted)
        },
        async {
          Log.d(TAG, "Subscribe to ${topicChange.added.asStrings()}")
          subscribeTo(topicChange.added)
        }
      ).awaitAll()
    }
  }

  companion object {
    private val TAG = FirebaseTopics::class.java.simpleName
  }
}
