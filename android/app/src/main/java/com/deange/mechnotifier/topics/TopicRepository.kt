package com.deange.mechnotifier.topics

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.deange.mechnotifier.dagger.Scope
import com.deange.mechnotifier.dagger.Scoped
import com.deange.mechnotifier.dagger.SingleInApp
import com.deange.mechnotifier.model.PostFilter
import com.deange.mechnotifier.model.PostFilter.Companion.ACCEPT_ALL
import com.deange.mechnotifier.settings.NoRegion
import com.tfcporciuncula.flow.FlowSharedPreferences
import com.tfcporciuncula.flow.Preference
import com.tfcporciuncula.flow.Serializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Stores the list of subscribed topics to disk.
 *
 * Topics can be set by calling [setTopics]. This is not an additive method; it *replaces* the set
 * of currently-subscribed topics. Consumers can listen to both the latest set of topics being
 * subscribed to, or the stream of topic changes between successive update calls.
 */
@SingleInApp
class TopicRepository
@Inject constructor(
  application: Application,
  topicCreator: TopicCreator,
  postFilterSerializer: Serializer<PostFilter>
) : Scoped {
  private val sharedPreferences: FlowSharedPreferences =
    FlowSharedPreferences(application.getSharedPreferences("topics", MODE_PRIVATE))

  private val defaultTopic = topicCreator.fromRegion(NoRegion, null)
  private val topics: Preference<Set<String>> =
    sharedPreferences.getStringSet("subscribed-topics", setOf(defaultTopic.name))
  private val filter: Preference<PostFilter> =
    sharedPreferences.getObject("post-filter", postFilterSerializer, ACCEPT_ALL)

  private var scope: Scope? = null
  private val topicChanges = MutableSharedFlow<TopicChange>()

  override fun onEnterScope(scope: Scope) {
    this.scope = scope
  }

  override fun onExitScope() {
    scope = null
  }

  fun setTopics(newTopics: Set<Topic>) {
    val currentTopics: Set<Topic> = topics.get().asTopics()

    val deleted: Set<Topic> = currentTopics - newTopics
    val added: Set<Topic> = newTopics - currentTopics

    topics.set(newTopics.asStrings())
    scope?.launch {
      topicChanges.emit(TopicChange(deleted = deleted, added = added))
    }
  }

  fun topics(): Flow<Set<Topic>> {
    return topics.asFlow().map { it.asTopics() }.distinctUntilChanged()
  }

  fun topicChanges(): Flow<TopicChange> {
    return topicChanges.filter { it.deleted.isNotEmpty() || it.added.isNotEmpty() }
  }

  fun setPostFilter(postFilter: PostFilter) {
    filter.set(postFilter)
  }

  fun postFilter(): Flow<PostFilter> {
    return filter.asFlow()
  }
}
