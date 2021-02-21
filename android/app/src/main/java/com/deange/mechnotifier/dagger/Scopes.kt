package com.deange.mechnotifier.dagger

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

/** Subscribes to the stream, disposing the subscription when the provided [Scope] is destroyed. */
fun <T> Observable<T>.subscribeWithScope(
  scope: Scope,
  onNext: (T) -> Unit
) = subscribeWithScope(scope, onNext, {})

/** Subscribes to the stream, disposing the subscription when the provided [Scope] is destroyed. */
fun <T> Observable<T>.subscribeWithScope(
  scope: Scope,
  onNext: (T) -> Unit,
  onError: (Throwable) -> Unit
) {
  val disposable = subscribe(onNext, onError)
  scope.onExit { disposable.dispose() }
}

/** Subscribes to the stream, disposing the subscription when the provided [Scope] is destroyed. */
fun <T> Maybe<T>.subscribeWithScope(
  scope: Scope,
  onNext: (T) -> Unit
) = subscribeWithScope(scope, onNext, {})

/** Subscribes to the stream, disposing the subscription when the provided [Scope] is destroyed. */
fun <T> Maybe<T>.subscribeWithScope(
  scope: Scope,
  onNext: (T) -> Unit,
  onError: (Throwable) -> Unit
) {
  val disposable = subscribe(onNext, onError)
  scope.onExit { disposable.dispose() }
}

/** Subscribes to the stream, disposing the subscription when the provided [Scope] is destroyed. */
fun <T> Single<T>.subscribeWithScope(
  scope: Scope,
  onNext: (T) -> Unit
) = subscribeWithScope(scope, onNext, {})

/** Subscribes to the stream, disposing the subscription when the provided [Scope] is destroyed. */
fun <T> Single<T>.subscribeWithScope(
  scope: Scope,
  onNext: (T) -> Unit,
  onError: (Throwable) -> Unit
) {
  val disposable = subscribe(onNext, onError)
  scope.onExit { disposable.dispose() }
}

/** Subscribes to the stream, disposing the subscription when the provided [Scope] is destroyed. */
fun <T> Flowable<T>.subscribeWithScope(
  scope: Scope,
  onNext: (T) -> Unit
) = subscribeWithScope(scope, onNext, {})

/** Subscribes to the stream, disposing the subscription when the provided [Scope] is destroyed. */
fun <T> Flowable<T>.subscribeWithScope(
  scope: Scope,
  onNext: (T) -> Unit,
  onError: (Throwable) -> Unit
) {
  val disposable = subscribe(onNext, onError)
  scope.onExit { disposable.dispose() }
}

/** Subscribes to the stream, disposing the subscription when the provided [Scope] is destroyed. */
fun Completable.subscribeWithScope(
  scope: Scope,
  onComplete: () -> Unit
) = subscribeWithScope(scope, onComplete, {})

/** Subscribes to the stream, disposing the subscription when the provided [Scope] is destroyed. */
fun Completable.subscribeWithScope(
  scope: Scope,
  onComplete: () -> Unit,
  onError: (Throwable) -> Unit
) {
  val disposable = subscribe(onComplete, onError)
  scope.onExit { disposable.dispose() }
}
