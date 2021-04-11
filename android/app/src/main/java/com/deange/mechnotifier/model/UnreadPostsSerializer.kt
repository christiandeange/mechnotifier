package com.deange.mechnotifier.model

import com.tfcporciuncula.flow.Serializer
import javax.inject.Inject

class UnreadPostsSerializer
@Inject constructor(
  private val postSerializer: PostSerializer
) : Serializer<UnreadPosts> {
  override fun deserialize(serialized: String): UnreadPosts {
    return postSerializer.unreadPosts().jsonToModel(serialized)
  }

  override fun serialize(value: UnreadPosts): String {
    return postSerializer.unreadPosts().modelToJson(value)
  }
}
