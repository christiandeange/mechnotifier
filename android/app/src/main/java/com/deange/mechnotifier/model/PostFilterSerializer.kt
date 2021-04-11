package com.deange.mechnotifier.model

import com.tfcporciuncula.flow.Serializer
import javax.inject.Inject

class PostFilterSerializer
@Inject constructor() : Serializer<PostFilter> {
  override fun deserialize(serialized: String): PostFilter {
    return PostFilter(serialized.split("|").map { PostType.valueOf(it) })
  }

  override fun serialize(value: PostFilter): String {
    return value.postTypes.joinToString("|") { it.name }
  }
}
