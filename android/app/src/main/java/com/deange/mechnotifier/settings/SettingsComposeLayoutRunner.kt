package com.deange.mechnotifier.settings

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deange.mechnotifier.R
import com.deange.mechnotifier.compose.BaseRegions
import com.deange.mechnotifier.compose.CollectionItemCheckbox
import com.deange.mechnotifier.compose.Empty
import com.deange.mechnotifier.compose.RegionPicker
import com.deange.mechnotifier.model.PostType
import com.deange.mechnotifier.model.PostType.BUYING
import com.deange.mechnotifier.model.PostType.SELLING
import com.deange.mechnotifier.model.PostType.TRADING
import com.deange.mechnotifier.topics.PublicType
import com.deange.mechnotifier.topics.PublicType.ARTISAN
import com.deange.mechnotifier.topics.PublicType.GROUP_BUY
import com.deange.mechnotifier.topics.PublicType.INTEREST_CHECK
import com.deange.mechnotifier.topics.PublicType.SERVICE
import com.deange.mechnotifier.topics.PublicType.VENDOR
import com.squareup.workflow1.ui.BuilderViewFactory
import com.squareup.workflow1.ui.bindShowRendering

val SettingsComposeLayoutRunner = BuilderViewFactory(
  type = SettingsScreen::class,
  viewConstructor = { initialRendering, initialViewEnv, context, _ ->
    ComposeView(context)
      .apply {
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        bindShowRendering(initialRendering, initialViewEnv) { rendering, _ ->
          setContent {
            Scaffold(
              topBar = {
                TopAppBar(
                  title = {
                    Text(text = stringResource(R.string.app_name))
                  },
                  actions = {
                    IconButton(onClick = rendering.onSaveClicked) {
                      Text(
                        text = stringResource(R.string.settings_save),
                        style = MaterialTheme.typography.button
                      )
                    }
                  }
                )
              },
              content = {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color(0xFFEDEDED)),
                  contentAlignment = TopCenter
                ) {
                  Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                      .widthIn(max = 600.dp)
                      .fillMaxHeight()
                      .padding(horizontal = 16.dp)
                      .verticalScroll(rememberScrollState())
                  ) {
                    Empty()
                    SettingsCard(label = stringResource(R.string.region_label)) {
                      val regions = BaseRegions + if (rendering.region is OtherRegion) {
                        rendering.region
                      } else {
                        OtherRegion(regionCode = "")
                      }

                      RegionPicker(
                        regions = regions,
                        selectedRegion = rendering.region,
                        selectedSubregion = rendering.subregion,
                        onRegionSelected = rendering.onRegionPicked,
                        customRegionError = rendering.customRegionError
                      )
                    }

                    SettingsCard(
                      label = stringResource(R.string.post_type_label),
                      footer = { PostTypesFooter(rendering) }
                    ) {
                      Column(
                        modifier = Modifier.alignByBaseline(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                      ) {
                        PostType.values().forEach { postType ->
                          CollectionItemCheckbox(
                            item = postType,
                            text = when (postType) {
                              BUYING -> stringResource(R.string.post_type_buying)
                              SELLING -> stringResource(R.string.post_type_selling)
                              TRADING -> stringResource(R.string.post_type_trading)
                            },
                            enabled = rendering.region !is NoRegion,
                            selectedItemTypes = rendering.selectedPostTypes,
                            onSelectedItemTypesChanged = rendering.onPostTypesChanged
                          )
                        }
                      }
                    }

                    SettingsCard(label = stringResource(R.string.public_post_type_label)) {
                      Column(
                        modifier = Modifier.alignByBaseline(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                      ) {
                        PublicType.values().forEach { publicType ->
                          CollectionItemCheckbox(
                            item = publicType,
                            text = when (publicType) {
                              INTEREST_CHECK -> stringResource(R.string.public_type_interest_check)
                              GROUP_BUY -> stringResource(R.string.public_type_group_buy)
                              ARTISAN -> stringResource(R.string.public_type_artisan)
                              SERVICE -> stringResource(R.string.public_type_service)
                              VENDOR -> stringResource(R.string.public_type_vendor)
                            },
                            enabled = true,
                            selectedItemTypes = rendering.selectedPublicTypes,
                            onSelectedItemTypesChanged = rendering.onPublicTypesChanged
                          )
                        }
                      }
                    }
                    Empty()
                  }
                }
              })
          }
        }
      }
  }
)

@Composable
private fun SettingsCard(
  label: String,
  header: @Composable () -> Unit = {},
  footer: @Composable () -> Unit = {},
  contents: @Composable RowScope.() -> Unit
) {
  Card(elevation = 4.dp) {
    Column(
      modifier = Modifier
        .padding(16.dp)
        .animateContentSize(),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      header()
      Row {
        Text(
          modifier = Modifier
            .width(144.dp)
            .alignByBaseline(),
          text = label
        )
        contents()
      }
      footer()
    }
  }
}

@Composable
private fun PostTypesFooter(rendering: SettingsScreen) {
  if (rendering.region !is NoRegion && rendering.selectedPostTypes.isEmpty()) {
    Text(
      text = stringResource(R.string.post_type_empty_warning),
      style = MaterialTheme.typography.caption
    )
  }
}
