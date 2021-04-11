package com.deange.mechnotifier.firebase

import com.deange.mechnotifier.topics.Topic
import com.deange.mechnotifier.topics.TopicChange
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
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
    unsubscribeFrom(topicChange.deleted)
    subscribeTo(topicChange.added)
  }
}
