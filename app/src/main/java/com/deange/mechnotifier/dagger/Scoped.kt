package com.deange.mechnotifier.dagger

/**
 * Represents an object that is bound to the lifecycle of a [Scope].
 *
 * Notified of the Scope's birth and death for managing any lifecycle-bound processes that should
 * end when the Scope does.
 */
interface Scoped {
  fun onEnterScope(scope: Scope)

  fun onExitScope() = Unit
}
