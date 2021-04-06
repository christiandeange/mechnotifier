package com.deange.mechnotifier.model

import com.deange.mechnotifier.model.PostType.BUYING
import com.deange.mechnotifier.model.PostType.SELLING
import com.deange.mechnotifier.model.PostType.TRADING
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlin.text.RegexOption.IGNORE_CASE

@JsonClass(generateAdapter = true)
data class Post(
  @Json(name = "post_id") val id: String,
  val title: String,
  val url: String,
  val flair: String?,
  @Json(name = "created_utc") val createdSeconds: Long
) : Comparable<Post> {
  @Transient val createdMillis: Long = createdSeconds * 1000

  fun type(): PostType {
    val postTags = tags()
    val haves = postTags["[H]"] ?: postTags["[h]"] ?: ""
    val wants = postTags["[W]"] ?: postTags["[w]"] ?: ""

    val hasMoney = haves.matches(MONEY_REGEX)
    val wantsMoney = wants.matches(MONEY_REGEX)

    return when {
      hasMoney && !wantsMoney -> BUYING
      !hasMoney && wantsMoney -> SELLING
      // This only makes sense if the post is a personal post, which is the only kind that this
      // app currently supports (eg: regional posts).
      else -> TRADING
    }
  }

  fun tags(): Map<String, String> {
    val tagRanges: List<IntRange> = TAG_REGEX.findAll(title).toList().map { it.range }

    val endsWithTag = tagRanges.last().last == title.length - 1

    val tagRangesZipped = if (endsWithTag) {
      tagRanges.zipWithNext()
    } else {
      val stringEnd = IntRange(title.length, title.length)
      (tagRanges + listOf(stringEnd)).zipWithNext()
    }
    // Grab the ranges between tags (or between the last tag and the end of the title).
    val valueRanges = tagRangesZipped
      .map { (range1, range2) -> IntRange(range1.last + 1, range2.first - 1) }

    val tags = tagRanges.map { title.substring(it).trim() }
    val values = valueRanges.map { title.substring(it).trim() }

    return tags.zip(values).toMap()
  }

  override fun compareTo(other: Post): Int {
    return createdMillis.compareTo(other.createdMillis)
  }

  private companion object {
    private val TAG_REGEX = Regex("""\[[^]]+\]""")
    private val MONEY_REGEX =
      Regex(""".*(cash|paypal|\$|ltc|฿|btc|bitcoin|money|monies|emt).*""", IGNORE_CASE)
  }
}
