package com.deange.mechnotifier.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Post(
  @Json(name = "post_id") val id: String,
  val title: String,
  val url: String,
  val flair: String?,
  @Json(name = "created_utc") val createdSeconds: Long
) : Comparable<Post> {
  @Transient val createdMillis: Long = createdSeconds * 1000

  override fun compareTo(other: Post): Int {
    return createdMillis.compareTo(other.createdMillis)
  }
}
