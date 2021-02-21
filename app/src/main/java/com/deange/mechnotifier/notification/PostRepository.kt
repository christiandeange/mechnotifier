package com.deange.mechnotifier.notification

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.deange.mechnotifier.dagger.SingleInApp
import com.deange.mechnotifier.model.Post
import com.deange.mechnotifier.model.UnreadPosts
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.Preference.Converter
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import javax.inject.Inject

@SingleInApp
class PostRepository
@Inject constructor(
  application: Application,
  unreadPostsConverter: Converter<UnreadPosts>
) {
  private val prefs: Preference<UnreadPosts> =
    RxSharedPreferences.create(application.getSharedPreferences("posts", MODE_PRIVATE))
        .getObject("unread-posts", UnreadPosts(emptyList()), unreadPostsConverter)

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

  fun unreadPosts(): Observable<UnreadPosts> {
    return prefs.asObservable().distinctUntilChanged()
  }
}
