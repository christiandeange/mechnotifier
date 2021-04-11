package com.deange.mechnotifier.dagger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

/** Subscribes to the stream, disposing the subscription when the provided [Scope] is destroyed. */
fun <T> Flow<T>.collectIn(
  scope: Scope,
  onNext: suspend (T) -> Unit
) = collectIn(scope, onNext, {})

/** Subscribes to the stream, disposing the subscription when the provided [Scope] is destroyed. */
fun <T> Flow<T>.collectIn(
  scope: Scope,
  onNext: suspend (T) -> Unit,
  onError: suspend (Throwable) -> Unit
) {
  scope.launch {
    catch { error -> onError(error) }.collect(onNext)
  }
}
