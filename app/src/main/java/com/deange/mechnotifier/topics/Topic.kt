package com.deange.mechnotifier.topics

/**
 * Represents a Firebase Cloud Messaging topic stream that clients can subscribe to.
 *
 * Since regions are a requirement for almost all mechmarket posts, topic subscriptions are the
 * primary manner in which clients will be notified of new posts.
 */
data class Topic(
  val name: String
)

/** Converts a set of [String]s to [Topic]s. */
fun Set<String>.asTopics(): Set<Topic> {
  return map(::Topic).toSet()
}

/** Converts a set of [Topic]s to [Strings]s. */
fun Set<Topic>.asStrings(): Set<String> {
  return map { it.name }.toSet()
}
