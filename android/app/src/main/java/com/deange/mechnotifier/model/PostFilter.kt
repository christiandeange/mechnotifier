package com.deange.mechnotifier.model

import android.util.Log
import com.deange.mechnotifier.topics.PublicType

class PostFilter(val postTypes: List<PostType>) {
  fun accept(post: Post): Boolean {
    val tags: List<String> = post.tags().keys.map { it.replace(Regex("""[\[\]]"""), "") }

    val type = post.type()
    val tagNames = PublicType.tagNames()

    Log.d(TAG, "Post types watched: $postTypes")
    Log.d(TAG, "Post ${post.id} type: $type, tags: $tags")

    val isPublicPost = (tags intersect tagNames).isNotEmpty()
    val isWatchedPostType = type in postTypes

    return (isPublicPost || isWatchedPostType).also { accepted ->
      Log.d(TAG, "Post ${post.id} accepted? $accepted")
    }
  }

  companion object {
    private val TAG = PostFilter::class.java.simpleName

    val ACCEPT_ALL = PostFilter(PostType.values().toList())
  }
}
