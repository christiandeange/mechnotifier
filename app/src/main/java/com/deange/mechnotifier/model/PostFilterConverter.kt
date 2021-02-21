package com.deange.mechnotifier.model

import com.f2prateek.rx.preferences2.Preference.Converter
import javax.inject.Inject

class PostFilterConverter
@Inject constructor() : Converter<PostFilter> {
  override fun deserialize(value: String): PostFilter {
    return PostFilter(value.split("|").map { PostType.valueOf(it) })
  }

  override fun serialize(filter: PostFilter): String {
    return filter.postTypes.joinToString("|") { it.name }
  }
}
