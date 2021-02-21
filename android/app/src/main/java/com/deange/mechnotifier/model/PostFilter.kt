package com.deange.mechnotifier.model

import com.deange.mechnotifier.topics.PublicType

class PostFilter(val postTypes: List<PostType>) {
  fun accept(post: Post): Boolean {
    val tags: List<String> = post.tags().keys.map { it.replace(Regex("""[\[\]]"""), "") }

    val tagNames = PublicType.tagNames()
    val isPublicPost = (tags intersect tagNames).isNotEmpty()

    return isPublicPost || post.type() in postTypes
  }

  companion object {
    val ACCEPT_ALL = PostFilter(PostType.values().toList())
  }
}
