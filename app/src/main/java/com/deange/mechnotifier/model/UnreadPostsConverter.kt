package com.deange.mechnotifier.model

import com.f2prateek.rx.preferences2.Preference.Converter
import javax.inject.Inject

class UnreadPostsConverter
@Inject constructor(
  private val postSerializer: PostSerializer
) : Converter<UnreadPosts> {
  override fun deserialize(json: String): UnreadPosts {
    return postSerializer.unreadPosts().jsonToModel(json)
  }

  override fun serialize(unreadPosts: UnreadPosts): String {
    return postSerializer.unreadPosts().modelToJson(unreadPosts)
  }
}
