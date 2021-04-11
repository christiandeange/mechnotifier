package com.deange.mechnotifier.dagger

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.runBlocking
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

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
) : Closeable {
  init {
    require(name.isNotBlank()) { "Provided scope name is blank" }
  }

  private val coroutineScope: CoroutineScope = if (parentScope == null) {
    CoroutineScope(CoroutineName(name) + Dispatchers.Main)
  } else {
    parentScope.coroutineScope + CoroutineName(name) + Dispatchers.Default
  }
  private val coroutineContext: CoroutineContext = coroutineScope.coroutineContext

  private val childScopes = mutableSetOf<Scope>()
  private val destroyed = Job()

  operator fun plus(childScopeName: String): Scope {
    check(destroyed.isActive) { "Scope ${toString()} has already been destroyed" }

    return Scope(parentScope = this, name = childScopeName).also { childScope ->
      childScopes += childScope
    }
  }

  fun destroy() {
    childScopes.forEach(Scope::destroy)
    destroyed.complete()
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

  fun launch(block: suspend CoroutineScope.() -> Unit): Job {
    return coroutineScope.launch(block = block)
  }

  fun <T> runBlocking(
    name: String = this.name,
    block: suspend CoroutineScope.() -> T
  ): T {
    return runBlocking(coroutineContext + CoroutineName(name), block)
  }

  companion object {
    val ROOT = Scope(null, "root")
  }
}
