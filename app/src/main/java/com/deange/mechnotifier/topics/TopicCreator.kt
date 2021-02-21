package com.deange.mechnotifier.topics

import com.deange.mechnotifier.settings.AnyRegion
import com.deange.mechnotifier.settings.CaSubregion
import com.deange.mechnotifier.settings.CanadaRegion
import com.deange.mechnotifier.settings.EuSubregion
import com.deange.mechnotifier.settings.EuropeRegion
import com.deange.mechnotifier.settings.NoRegion
import com.deange.mechnotifier.settings.OtherRegion
import com.deange.mechnotifier.settings.Region
import com.deange.mechnotifier.settings.Subregion
import com.deange.mechnotifier.settings.UsSubregion
import com.deange.mechnotifier.settings.UsaRegion
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

  fun toRegionOrNull(topic: Topic): Pair<Region, Subregion?>? {
    val parts = topic.name.split("-")
    return when (parts.size) {
      1 -> {
        val region: Region = findRegion(parts[0]) ?: return null
        region to null
      }
      2 -> {
        val region: Region = findRegion(parts[0]) ?: return null
        region to findSubregion(region, parts[1])
      }
      else -> {
        null
      }
    }
  }

  fun fromPublicType(publicType: PublicType): Topic {
    return Topic(publicType.tagName)
  }

  fun toPublicTypeOrNull(topic: Topic): PublicType? {
    return if (topic.name in PublicType.tagNames()) {
      PublicType.fromTagName(topic.name)
    } else {
      null
    }
  }

  private fun findRegion(regionCode: String): Region? {
    return when (regionCode) {
      "none" -> NoRegion
      "any" -> AnyRegion
      "CA" -> CanadaRegion
      "EU" -> EuropeRegion
      "US" -> UsaRegion
      in PublicType.tagNames() -> null
      else -> OtherRegion(regionCode)
    }
  }

  private fun findSubregion(
    region: Region,
    subregionCode: String
  ): Subregion {
    return when (region) {
      is NoRegion,
      is AnyRegion -> error("$region cannot be linked to any subregions.")
      is CanadaRegion -> CaSubregion.values().first { it.subregionCode == subregionCode }
      is EuropeRegion -> EuSubregion.values().first { it.subregionCode == subregionCode }
      is UsaRegion -> UsSubregion.values().first { it.subregionCode == subregionCode }
      is OtherRegion -> error("$region cannot be linked to any subregions.")
    }
  }
}
