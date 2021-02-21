package com.deange.mechnotifier.topics

/**
 * Encapsulates information about a change of topic subscriptions.
 */
data class TopicChange(
  val deleted: Set<Topic>,
  val added: Set<Topic>
) {
  init {
    require((added intersect deleted).isEmpty()) {
      "Elements are both added and deleted: ${added intersect deleted}"
    }
  }
}
