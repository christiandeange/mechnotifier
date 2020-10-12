package com.deange.mechnotifier.view

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener

abstract class SimpleItemSelectedListener : OnItemSelectedListener {
  final override fun onItemSelected(
    parent: AdapterView<*>,
    view: View?,
    position: Int,
    id: Long
  ) = onItemSelected(position)

  override fun onNothingSelected(parent: AdapterView<*>) = Unit

  abstract fun onItemSelected(position: Int)
}
