package com.deange.mechnotifier.view

import androidx.annotation.StringRes

/** An object that has a constant name representable by a translatable string. */
interface Nameable {
  @get:StringRes val nameResId: Int
}
