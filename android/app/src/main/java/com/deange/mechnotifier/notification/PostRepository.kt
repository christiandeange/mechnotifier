package com.deange.mechnotifier.notification

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.deange.mechnotifier.dagger.SingleInApp
import com.deange.mechnotifier.model.Post
import com.deange.mechnotifier.model.UnreadPosts
import com.tfcporciuncula.flow.FlowSharedPreferences
import com.tfcporciuncula.flow.Preference
import com.tfcporciuncula.flow.Serializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@SingleInApp
class PostRepository
@Inject constructor(
  application: Application,
  unreadPostsSerializer: Serializer<UnreadPosts>
) {
  private val prefs: Preference<UnreadPosts> =
    FlowSharedPreferences(application.getSharedPreferences("posts", MODE_PRIVATE))
      .getObject("unread-posts", unreadPostsSerializer, UnreadPosts(emptyList()))

  fun markAllAsRead() {
    setUnreads(UnreadPosts(emptyList()))
  }

  fun markAsRead(postId: String) {
    setUnreads(prefs.get().read(postId))
  }

  fun addUnread(post: Post) {
    setUnreads(prefs.get() + post)
  }

  private fun setUnreads(unreadPosts: UnreadPosts) {
    prefs.set(unreadPosts)
  }

  fun unreadPosts(): Flow<UnreadPosts> {
    return prefs.asFlow().distinctUntilChanged()
  }
}
