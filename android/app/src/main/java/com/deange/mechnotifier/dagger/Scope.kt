package com.deange.mechnotifier.dagger

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.disposables.Disposable
import java.io.Closeable

/**
 * A concrete unit in the lifecycle of the global application.
 *
 * Scopes can be nested into a parent scope, which can create multiple child scopes. Scopes can have
 * services registered to them via [Scoped] to perform lifecycle-aware functions.
 */
class Scope
private constructor(
  private val parentScope: Scope?,
  val name: String
): Closeable {
  init {
    require(name.isNotBlank()) { "Provided scope name is blank" }
  }

  private val childScopes = mutableSetOf<Scope>()
  private val disposables = mutableSetOf<Disposable>()
  private val destroyed = BehaviorRelay.create<Unit>()

  operator fun plus(childScopeName: String): Scope {
    check(!destroyed.hasValue()) { "Scope ${toString()} has already been destroyed" }

    return Scope(parentScope = this, name = childScopeName).also { childScope ->
      childScopes += childScope
    }
  }

  fun onExit(block: (Scope) -> Unit) {
    disposables += destroyed.subscribe { block(this) }
  }

  fun destroy() {
    childScopes.forEach(Scope::destroy)

    destroyed.accept(Unit)
    disposables.forEach(Disposable::dispose)
  }

  override fun toString(): String {
    return if (parentScope == null) {
      name
    } else {
      "$parentScope >>> $name"
    }
  }

  /**
   * Allows the [Closeable.use { }][kotlin.io.use] extension to be applied to a Scope,
   * [destroying][destroy] it after the lambda block has completed.
   */
  override fun close() = destroy()

  companion object {
    val ROOT = Scope(null, "root")
  }
}
