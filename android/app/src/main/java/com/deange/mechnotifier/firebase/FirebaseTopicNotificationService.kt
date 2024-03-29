package com.deange.mechnotifier.firebase

import android.util.Log
import com.deange.mechnotifier.dagger.Scope
import com.deange.mechnotifier.mainApplication
import com.deange.mechnotifier.model.Post
import com.deange.mechnotifier.model.PostSerializer
import com.deange.mechnotifier.notification.NotificationPublisher
import com.deange.mechnotifier.notification.PostRepository
import com.deange.mechnotifier.topics.Topic
import com.deange.mechnotifier.topics.TopicRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FirebaseTopicNotificationService : FirebaseMessagingService() {
  private lateinit var scope: Scope

  @Inject lateinit var topicRepository: TopicRepository
  @Inject lateinit var firebaseTopics: FirebaseTopics
  @Inject lateinit var postSerializer: PostSerializer
  @Inject lateinit var notificationPublisher: NotificationPublisher
  @Inject lateinit var postRepository: PostRepository

  override fun onCreate() {
    super.onCreate()

    mainApplication.appComponent.inject(this)
    scope = mainApplication.scope + this::class.java.name
  }

  override fun onMessageReceived(message: RemoteMessage) {
    Log.d(TAG, "onMessageReceived + ${message.data}")

    val dataMap: Map<String, *> = message.data.toMap()
    val post: Post = postSerializer.posts().jsonToModel(dataMap)

    scope.launch {
      val filter = topicRepository.postFilter().first()
      if (filter.accept(post)) {
        postRepository.addUnread(post)
        notificationPublisher.showNotifications(
          unreadPosts = postRepository.unreadPosts().first(),
          newUnreadPost = post
        )
      }
    }
  }

  override fun onNewToken(token: String) {
    Log.d(TAG, "onNewToken = $token")

    // If we are issued a new push token, we need to resubscribe to the same topics.
    scope.launch {
      val topics: Set<Topic> = topicRepository.topics().first()
      firebaseTopics.subscribeTo(topics)
      Log.d(TAG, "Subscribed to $topics after token refresh.")
    }
  }

  override fun onDestroy() {
    scope.destroy()
    super.onDestroy()
  }

  private companion object {
    private val TAG = FirebaseTopicNotificationService::class.java.simpleName
  }
}
