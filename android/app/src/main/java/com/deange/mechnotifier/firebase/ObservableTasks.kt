package com.deange.mechnotifier.firebase

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Converts the Task to a Maybe stream, handling the following cases:
 *
 * - If the task completes successfully with result of type [T], that result is emitted.
 * - If the task completes successfully where [T] is [Void], the stream is completed.
 * - If the task fails with an exception, the stream propagates the exception.
 * - If the task is canceled, the stream is completed.
 *
 * This will wait synchronously forever for the task to enter any of the aforementioned states.
 * Consumers should apply their own stream timeout if they want to avoid waiting indefinitely.
 */
fun <T : Any> Task<T>.asMaybe(): Maybe<T> {
  return Maybe.create { emitter ->
    addOnCompleteListener {
      when {
        result != null -> {
          // The task completed successfully with an explicit result.
          emitter.onSuccess(result!!)
        }
        exception != null -> {
          // The task failed with an exception.
          emitter.onError(exception!!)
        }
        isCanceled -> {
          // The task was cancelled, producing no result.
          emitter.onComplete()
        }
        isComplete -> {
          // The task completed, but with not result. This applies to Task<Void> instances.
          emitter.onComplete()
        }
        else -> {
          // An unknown state has been entered.
          error("Unknown state: no result, no exception, not complete, not canceled.")
        }
      }
    }

    // Waits synchronously forever for the task to finish.
    // Consumers should apply their own timeout if they want to avoid waiting indefinitely.
    Tasks.await(this)
  }
}

/** @see [asMaybe] */
fun <T : Any> Task<T>.asObservable(): Observable<T> = asMaybe().toObservable()

/** @see [asMaybe] */
fun <T : Any> Task<T>.asSingle(): Single<T> = asMaybe().toSingle()

/** @see [asMaybe] */
fun <T : Any> Task<T>.asFlowable(): Flowable<T> = asMaybe().toFlowable()

/** @see [asMaybe] */
fun <T : Any> Task<T>.asCompletable(): Completable = asMaybe().ignoreElement()
