package com.deange.mechnotifier.topics

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.deange.mechnotifier.dagger.SingleInApp
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
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
  application: Application
) {
  private val prefs: Preference<Set<String>> =
    RxSharedPreferences.create(application.getSharedPreferences("topics", MODE_PRIVATE))
        .getStringSet("subscribed-topics")

  private val topicChanges = PublishRelay.create<TopicChange>()

  fun setTopics(newTopics: Set<Topic>) {
    val currentTopics: Set<Topic> = prefs.get().asTopics()

    val deleted: Set<Topic> = currentTopics - newTopics
    val added: Set<Topic> = newTopics - currentTopics

    prefs.set(newTopics.asStrings())
    topicChanges.accept(TopicChange(deleted = deleted, added = added))
  }

  fun topics(): Observable<Set<Topic>> {
    return prefs.asObservable().map { it.asTopics() }.distinctUntilChanged()
  }

  fun topicChanges(): Observable<TopicChange> {
    return topicChanges.hide().filter { it.deleted.isNotEmpty() || it.added.isNotEmpty() }
  }
}
