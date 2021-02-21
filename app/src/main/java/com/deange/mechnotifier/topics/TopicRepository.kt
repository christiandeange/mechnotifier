package com.deange.mechnotifier.topics

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.deange.mechnotifier.dagger.SingleInApp
import com.deange.mechnotifier.model.PostFilter
import com.deange.mechnotifier.model.PostFilter.Companion.ACCEPT_ALL
import com.deange.mechnotifier.settings.NoRegion
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.Preference.Converter
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Stores the list of subscribed topics to disk.
 *
 * Topics can be set by calling [setTopic]. This is not an additive method; it *replaces* the set
 * of currently-subscribed topics. Consumers can listen to both the latest set of topics being
 * subscribed to, or the stream of topic changes between successive update calls.
 */
@SingleInApp
class TopicRepository
@Inject constructor(
  application: Application,
  topicCreator: TopicCreator,
  postFilterConverter: Converter<PostFilter>
) {
  private val sharedPreferences: RxSharedPreferences =
    RxSharedPreferences.create(application.getSharedPreferences("topics", MODE_PRIVATE))

  private val defaultTopic = topicCreator.fromRegion(NoRegion, null)
  private val topics: Preference<Set<String>> =
    sharedPreferences.getStringSet("subscribed-topics", setOf(defaultTopic.name))
  private val filter: Preference<PostFilter> =
    sharedPreferences.getObject("post-filter", ACCEPT_ALL, postFilterConverter)

  private val topicChanges = PublishRelay.create<TopicChange>()

  fun setTopics(newTopics: Set<Topic>) {
    val currentTopics: Set<Topic> = topics.get().asTopics()

    val deleted: Set<Topic> = currentTopics - newTopics
    val added: Set<Topic> = newTopics - currentTopics

    topics.set(newTopics.asStrings())
    topicChanges.accept(TopicChange(deleted = deleted, added = added))
  }

  fun topics(): Observable<Set<Topic>> {
    return topics.asObservable().map { it.asTopics() }.distinctUntilChanged()
  }

  fun topicChanges(): Observable<TopicChange> {
    return topicChanges.hide().filter { it.deleted.isNotEmpty() || it.added.isNotEmpty() }
  }

  fun setPostFilter(postFilter: PostFilter) {
    filter.set(postFilter)
  }

  fun postFilter(): Observable<PostFilter> {
    return filter.asObservable()
  }
}
