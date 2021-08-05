package com.deange.mechnotifier.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T : Any> CollectionItemCheckbox(
  item: T,
  text: String,
  enabled: Boolean,
  selectedItemTypes: List<T>,
  onSelectedItemTypesChanged: (List<T>) -> Unit
) {
  val checkedState = remember(item) {
    mutableStateOf(item in selectedItemTypes)
  }

  fun setSelected(isSelected: Boolean) {
    val newTypes = if (isSelected) {
      selectedItemTypes + item
    } else {
      selectedItemTypes - item
    }.distinct()

    checkedState.value = isSelected
    onSelectedItemTypesChanged(newTypes)
  }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(),
        onClick = { setSelected(!checkedState.value) }
      ),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Checkbox(
      modifier = Modifier.padding(end = 8.dp),
      checked = checkedState.value,
      enabled = enabled,
      onCheckedChange = { selected -> setSelected(selected) },
    )
    Text(
      text = text,
      modifier = Modifier.padding(
        top = ButtonDefaults.ContentPadding.calculateTopPadding(),
        bottom = ButtonDefaults.ContentPadding.calculateBottomPadding()
      )
    )
  }
}
