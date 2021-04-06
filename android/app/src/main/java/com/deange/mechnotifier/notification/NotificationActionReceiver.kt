package com.deange.mechnotifier.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import com.deange.mechnotifier.mainApplication
import com.deange.mechnotifier.model.Post
import com.deange.mechnotifier.model.UnreadPosts
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

class NotificationActionReceiver : BroadcastReceiver() {
  @Inject lateinit var postRepository: PostRepository
  @Inject lateinit var notificationPublisher: NotificationPublisher

  override fun onReceive(
    context: Context,
    intent: Intent
  ) {
    context.mainApplication.appComponent.inject(this)
    val scope = context.mainApplication.scope + this::class.java.name

    scope.use {
      when (intent.action) {
        INTENT_ACTION_MARK_POST_SEEN -> {
          markAsRead(intent.getStringExtra(INTENT_EXTRA_KEY_POST_ID)!!)
        }
        INTENT_ACTION_MARK_ALL_SEEN -> {
          markAllAsRead()
        }
        INTENT_ACTION_OPEN_POST -> {
          markAsRead(intent.getStringExtra(INTENT_EXTRA_KEY_POST_ID)!!)
          openUrl(context, intent.getStringExtra(INTENT_EXTRA_KEY_POST_URL)!!)
        }
        else -> {
          return@use
        }
      }

      // We're in a BroadcastReceiver here still, so we can't actually perform any async work.
      // Fortunately the preference should always emit the currently-stored value, so this should
      // be synchronous. Adding a 2-second timeout ensures, in an absolute worst-case scenario,
      // that we don't lock the main thread for long enough to cause an ANR.
      val unreadPosts = postRepository.unreadPosts().timeout(2, SECONDS).blockingFirst()
      notificationPublisher.showNotifications(unreadPosts, newUnreadPost = null)
    }
  }

  private fun markAsRead(postId: String) {
    postRepository.markAsRead(postId)
  }

  private fun markAllAsRead() {
    postRepository.markAllAsRead()
  }

  private fun openUrl(context: Context, url: String) {
    // FLAG_ACTIVITY_NEW_TASK is required for starting an Activity from a non-Activity component.
    context.startActivity(
      Intent(Intent.ACTION_VIEW, Uri.parse(url))
        .addFlags(FLAG_ACTIVITY_NEW_TASK)
    )
  }

  companion object {
    private const val INTENT_ACTION_MARK_POST_SEEN = "com.deange.mechnotifier.action.MARK_SEEN"
    private const val INTENT_ACTION_MARK_ALL_SEEN = "com.deange.mechnotifier.action.MARK_ALL_SEEN"
    private const val INTENT_ACTION_OPEN_POST = "com.deange.mechnotifier.action.OPEN"

    private const val INTENT_EXTRA_KEY_POST_ID = "key-post-id"
    private const val INTENT_EXTRA_KEY_POST_URL = "key-post-url"

    fun createDeleteIntent(
      context: Context,
      post: Post
    ): Intent {
      return Intent(context, NotificationActionReceiver::class.java)
        .setAction(INTENT_ACTION_MARK_POST_SEEN)
        .putExtra(INTENT_EXTRA_KEY_POST_ID, post.id)
    }

    fun createDeleteAllIntent(context: Context): Intent {
      return Intent(context, NotificationActionReceiver::class.java)
        .setAction(INTENT_ACTION_MARK_ALL_SEEN)
    }

    fun createOpenIntent(
      context: Context,
      post: Post
    ): Intent {
      return Intent(context, NotificationActionReceiver::class.java)
        .setAction(INTENT_ACTION_OPEN_POST)
        .putExtra(INTENT_EXTRA_KEY_POST_ID, post.id)
        .putExtra(INTENT_EXTRA_KEY_POST_URL, post.url)
    }
  }
}
