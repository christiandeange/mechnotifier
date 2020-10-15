package com.deange.mechnotifier.topics

/**
 * Encapsulates information about a change of topic subscriptions.
 */
data class TopicChange(
  val deleted: Set<Topic>,
  val added: Set<Topic>
) {
  init {
    val intersection: Set<Topic> = added intersect deleted
    require(intersection.isEmpty()) {
      "Elements are both deleted and added: ${intersection.asStrings()}"
    }
  }
}
