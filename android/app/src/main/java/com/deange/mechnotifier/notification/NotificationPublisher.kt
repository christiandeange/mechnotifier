package com.deange.mechnotifier.notification

import android.app.Application
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.BigTextStyle
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import androidx.core.app.NotificationManagerCompat
import com.deange.mechnotifier.R
import com.deange.mechnotifier.model.Post
import com.deange.mechnotifier.notification.NotificationActionReceiver.Companion.createDeleteAllIntent
import com.deange.mechnotifier.notification.NotificationActionReceiver.Companion.createDeleteIntent
import com.deange.mechnotifier.notification.NotificationActionReceiver.Companion.createOpenIntent
import com.deange.mechnotifier.notification.NotificationChannels.Companion.POSTS_CHANNEL
import javax.inject.Inject

class NotificationPublisher
@Inject constructor(
  private val application: Application,
) {
  private val notificationManager = NotificationManagerCompat.from(application)

  fun showNotifications(
    unreadPosts: List<Post>,
    newUnreadPost: Post?
  ) {
    when (unreadPosts.count()) {
      0 -> showNoNotifications()
      1 -> showOneNotification(unreadPosts.single(), newUnreadPost)
      else -> showMultipleNotifications(unreadPosts, newUnreadPost)
    }
  }

  private fun showNoNotifications() {
    notificationManager.cancelAll()
  }

  private fun showOneNotification(
    post: Post,
    newUnreadPost: Post?
  ) {
    val (id, notification) = createPostNotification(
      post = post,
      groupKey = null,
      withSounds = newUnreadPost == post
    )

    notificationManager.notify(id, notification)
    notificationManager.cancel(GROUP_SUMMARY_ID)
  }

  private fun showMultipleNotifications(
    posts: List<Post>,
    newUnreadPost: Post?
  ) {
    val summary: Notification = createSummaryNotification(
      postCount = posts.count(),
      withSounds = newUnreadPost != null && newUnreadPost in posts
    )

    notificationManager.notify(GROUP_SUMMARY_ID, summary)

    posts.forEach { post ->
      val (id, notification) = createPostNotification(
        post = post,
        groupKey = POSTS_CHANNEL,
        withSounds = false
      )
      notificationManager.notify(id, notification)
    }
  }

  private fun createSummaryNotification(
    postCount: Int,
    withSounds: Boolean
  ): Notification {
    val deleteIntent: Intent = createDeleteAllIntent(application)
    val pendingDeleteIntent: PendingIntent =
      PendingIntent.getBroadcast(application, GROUP_SUMMARY_ID, deleteIntent, 0)

    return NotificationCompat.Builder(application, POSTS_CHANNEL)
      .setAllowSystemGeneratedContextualActions(true)
      .setContentText(application.getString(R.string.notification_group_summary, postCount))
      .setDeleteIntent(pendingDeleteIntent)
      .setGroup(POSTS_CHANNEL)
      .setGroupSummary(true)
      .setPriority(PRIORITY_DEFAULT)
      .setSmallIcon(R.drawable.ic_baseline_arrow_upward_24)
      .apply {
        if (!withSounds) {
          setNotificationSilent()
        }
      }
      .build()
  }

  private fun createPostNotification(
    post: Post,
    groupKey: String?,
    withSounds: Boolean
  ): Pair<Int, Notification> {
    val id: Int = post.id.hashCode()

    val contentIntent = createOpenIntent(application, post)
    val pendingContentIntent: PendingIntent =
      PendingIntent.getBroadcast(application, id, contentIntent, 0)

    val deleteIntent: Intent = createDeleteIntent(application, post)
    val pendingDeleteIntent: PendingIntent =
      PendingIntent.getBroadcast(application, -id, deleteIntent, 0)

    val notification: Notification =
      NotificationCompat.Builder(application, POSTS_CHANNEL)
        .setAllowSystemGeneratedContextualActions(true)
        .setAutoCancel(true)
        .setContentIntent(pendingContentIntent)
        .setContentText(post.title)
        .setDeleteIntent(pendingDeleteIntent)
        .setGroup(groupKey)
        .setOnlyAlertOnce(true)
        .setPriority(PRIORITY_DEFAULT)
        .setSmallIcon(R.drawable.ic_baseline_arrow_upward_24)
        .setStyle(BigTextStyle().bigText(post.title))
        .setWhen(post.createdMillis)
        .apply {
          if (!withSounds) {
            setNotificationSilent()
          }
        }
        .build()

    return id to notification
  }

  private companion object {
    const val GROUP_SUMMARY_ID: Int = 0xdeadbeef.toInt()
  }
}
