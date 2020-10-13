package com.deange.mechnotifier.topics

/**
 * Encapsulates information about a change of topic subscriptions.
 */
data class TopicChange(
  val deleted: Set<Topic>,
  val added: Set<Topic>
)
