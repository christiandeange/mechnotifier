package com.deange.mechnotifier.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlin.properties.Delegates.observable

class NameableAdapter<T : Nameable>(
  context: Context,
  items: List<T> = emptyList()
) : BaseAdapter() {
  private val inflater: LayoutInflater = LayoutInflater.from(context)

  var items: List<T> by observable(items) { _, _, _ -> notifyDataSetChanged() }

  override fun getCount(): Int = items.size

  override fun getItem(position: Int): T = items[position]

  override fun getItemId(position: Int): Long = getItem(position).hashCode().toLong()

  override fun getView(
    position: Int,
    convertView: View?,
    parent: ViewGroup
  ): View {
    return createView(position, convertView, parent, android.R.layout.simple_spinner_item)
  }

  override fun getDropDownView(
    position: Int,
    convertView: View?,
    parent: ViewGroup
  ): View {
    return createView(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item)
  }

  private fun createView(
    position: Int,
    convertView: View?,
    parent: ViewGroup,
    resource: Int
  ): View {
    return ((convertView ?: inflater.inflate(resource, parent, false)) as TextView)
      .also { it.setText(getItem(position).nameResId) }
  }
}
