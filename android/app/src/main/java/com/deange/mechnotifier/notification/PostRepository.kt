package com.deange.mechnotifier.notification

import com.deange.mechnotifier.dagger.SingleInApp
import com.deange.mechnotifier.db.PostDao
import com.deange.mechnotifier.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@SingleInApp
class PostRepository
@Inject constructor(
  private val postDao: PostDao
) {
  suspend fun markAllAsRead() {
    val allPosts = postDao.posts()
    val allPostsAsRead = allPosts.map { it.copy(unread = false) }
    postDao.updatePosts(allPostsAsRead)
  }

  suspend fun markAsRead(postId: String) {
    postDao.post(postId)?.let { post ->
      postDao.updatePost(post.copy(unread = false))
    }
  }

  suspend fun addUnread(post: Post) {
    postDao.insertPost(post.copy(unread = true))
  }

  fun unreadPosts(): Flow<List<Post>> {
    return postDao.postsFlow()
      .map { posts -> posts.filter { it.unread } }
      .distinctUntilChanged()
  }
}
