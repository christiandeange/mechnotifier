package com.deange.mechnotifier.settings

import androidx.annotation.StringRes
import com.deange.mechnotifier.R
import com.deange.mechnotifier.view.Nameable

/**
 * A top-level region in which posts can be filtered by.
 *
 * Certain regions have a finite list of [Subregion]s that can be used to identify a more granular
 * area. For places outside of those zones, [OtherRegion] represents any other possible region
 * identified by its [two-letter country code](https://wikipedia.org/wiki/ISO_3166-1_alpha-2).
 *
 * See [the wiki](https://www.reddit.com/r/mechmarket/wiki/rules/rules#wiki_title_requirements)
 * for more details on how regions and subregions are determined.
 */
sealed class Region(
  @StringRes override val nameResId: Int,
  open val countryCode: String,
  val subregions: List<Subregion> = emptyList()
) : Nameable {
  fun hasSubregions(): Boolean = subregions.isNotEmpty()

  override fun toString(): String = countryCode
}

object AnyRegion : Region(R.string.region_any, "any") {
  override fun toString(): String = "Any Region"
}

object CanadaRegion : Region(R.string.region_ca, "CA", CaSubregion.values().toList())

object EuropeRegion : Region(R.string.region_eu, "EU", EuSubregion.values().toList())

object UsaRegion : Region(R.string.region_us, "US", UsSubregion.values().toList())

data class OtherRegion(
  override val countryCode: String
) : Region(R.string.region_other, countryCode)
