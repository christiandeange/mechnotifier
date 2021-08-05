package com.deange.mechnotifier.view

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Abstraction for text content.
 *
 * This is useful for creating text-based content in business logic that might use a resource id,
 * for the purpose of delaying evaluation of those resource strings until they are passed into the
 * view layer, where a themed [Context] is readily available.
 */
interface Text {
  /** Computes the value of the text. */
  fun evaluate(context: Context): CharSequence

  /** Computes the value of the text during a composition. */
  @Composable fun evaluate(): CharSequence = evaluate(LocalContext.current)

  /** Concatenates two [Text] objects with no separator. */
  operator fun plus(otherText: Text): Text = CompoundText(this, otherText)

  companion object {
    /** Returns a [Text] object with a hardcoded value. */
    operator fun invoke(constantText: CharSequence): Text = ConstantText(constantText)

    /** Returns a [Text] object that has a value derived from a string resource. */
    operator fun invoke(@StringRes stringResId: Int): Text = ResourceText(stringResId)
  }
}

private class ConstantText(private val constantText: CharSequence) : Text {
  override fun evaluate(context: Context): CharSequence = constantText
}

private class ResourceText(@StringRes private val stringResId: Int) : Text {
  override fun evaluate(context: Context): CharSequence = context.getText(stringResId)
}

private class CompoundText(
  private vararg val textValues: Text
) : Text {
  override fun evaluate(context: Context): CharSequence {
    return textValues.joinToString { it.evaluate(context) }
  }
}
