package com.deange.mechnotifier.firebase

import androidx.annotation.CheckResult
import com.deange.mechnotifier.topics.Topic
import com.deange.mechnotifier.topics.TopicChange
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FirebaseTopics
@Inject constructor(
) {
  private val firebase: FirebaseMessaging = FirebaseMessaging.getInstance()

  @CheckResult
  fun unsubscribeFrom(topic: Topic): Completable = unsubscribeFrom(setOf(topic))

  @CheckResult
  fun unsubscribeFrom(topics: Set<Topic>): Completable {
    val unsubscribeTasks: List<Task<Void>> = topics.map { topic ->
      firebase.unsubscribeFromTopic(topic.name)
    }

    return Tasks.whenAll(unsubscribeTasks)
        .asCompletable()
        .subscribeOn(Schedulers.io())
  }

  @CheckResult
  fun subscribeTo(topic: Topic): Completable = subscribeTo(setOf(topic))

  @CheckResult
  fun subscribeTo(topics: Set<Topic>): Completable {
    val subscribeTasks: List<Task<Void>> = topics.map { topic ->
      firebase.subscribeToTopic(topic.name)
    }

    return Tasks.whenAll(subscribeTasks)
        .asCompletable()
        .subscribeOn(Schedulers.io())
  }

  @CheckResult
  fun applyChangesFrom(topicChange: TopicChange): Completable {
    val allTasks = listOf(
        unsubscribeFrom(topicChange.deleted),
        subscribeTo(topicChange.added)
    )

    return Completable.merge(allTasks)
  }
}
