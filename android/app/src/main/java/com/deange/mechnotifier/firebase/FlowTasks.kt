package com.deange.mechnotifier.firebase

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive

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
fun <T : Any> Task<T>.asFlow(): Flow<T> {
  return callbackFlow {
    addOnCompleteListener {
      when {
        result != null -> {
          if (isActive) {
            // The task completed successfully with an explicit result.
            sendBlocking(result!!)
          }
        }
        exception != null -> {
          // The task failed with an exception.
          close(exception!!)
        }
        isCanceled -> {
          // The task was cancelled, producing no result.
          close()
        }
        isComplete -> {
          // The task completed, but with not result. This applies to Task<Void> instances.
          close()
        }
        else -> {
          // An unknown state has been entered.
          close(
            IllegalStateException(
              "Unknown state: no result, no exception, not complete, not canceled."
            )
          )
        }
      }
    }

    // Waits synchronously forever for the task to finish.
    // Consumers should apply their own timeout if they want to avoid waiting indefinitely.
    @Suppress("BlockingMethodInNonBlockingContext")
    Tasks.await(this@asFlow)

    awaitClose()
  }.flowOn(Dispatchers.IO)
}
