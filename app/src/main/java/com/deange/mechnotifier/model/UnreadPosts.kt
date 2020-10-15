package com.deange.mechnotifier.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnreadPosts(
  val posts: List<Post>
) {
  operator fun plus(post: Post) = UnreadPosts((posts + post).toSet().sortedDescending())

  fun read(postId: String) = UnreadPosts(posts.filter { it.id != postId })
}
