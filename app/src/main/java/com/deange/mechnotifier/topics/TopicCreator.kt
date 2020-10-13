package com.deange.mechnotifier.topics

import com.deange.mechnotifier.settings.Region
import com.deange.mechnotifier.settings.Subregion
import javax.inject.Inject

/**
 * Creates a topic from various parameters provided.
 *
 * This translates between the way in which posts can be filtered on the client to the way that
 * the backend server notifies clients of new posts.
 */
class TopicCreator
@Inject constructor() {
  fun fromRegion(
    region: Region,
    subregion: Subregion?
  ): Topic {
    return if (region.hasSubregions() && subregion != null && !subregion.isAllSubregions) {
      Topic("${region.regionCode}-${subregion.subregionCode}")
    } else {
      Topic(region.regionCode)
    }
  }
}
