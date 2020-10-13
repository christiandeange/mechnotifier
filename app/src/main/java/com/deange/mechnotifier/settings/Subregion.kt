package com.deange.mechnotifier.settings

import androidx.annotation.StringRes
import com.deange.mechnotifier.R
import com.deange.mechnotifier.view.Nameable

/**
 * A sublocality belonging to a [Region].
 *
 * Certain regions are divided into smaller localities with known values. For those given regions,
 * only subregions with known values are acceptable.
 *
 * See [the wiki](https://www.reddit.com/r/mechmarket/wiki/rules/rules#wiki_title_requirements)
 * for more details on how regions and subregions are determined.
 */
interface Subregion : Nameable {
  val subregionCode: String
  val isAllSubregions: Boolean
}

enum class CaSubregion(
  @StringRes override val nameResId: Int,
  override val isAllSubregions: Boolean = false
) : Subregion {
  ALL(R.string.subregion_ca_all, isAllSubregions = true),
  AB(R.string.subregion_ca_ab),
  BC(R.string.subregion_ca_bc),
  MB(R.string.subregion_ca_mb),
  NB(R.string.subregion_ca_nb),
  NL(R.string.subregion_ca_nl),
  NT(R.string.subregion_ca_nt),
  NS(R.string.subregion_ca_ns),
  NU(R.string.subregion_ca_nu),
  ON(R.string.subregion_ca_on),
  PE(R.string.subregion_ca_pe),
  QC(R.string.subregion_ca_qc),
  SK(R.string.subregion_ca_sk),
  YT(R.string.subregion_ca_yt),
  ;

  override val subregionCode: String = name
}

enum class EuSubregion(
  @StringRes override val nameResId: Int,
  override val isAllSubregions: Boolean = false
) : Subregion {
  ALL(R.string.subregion_eu_all, isAllSubregions = true),
  AL(R.string.subregion_eu_al),
  AD(R.string.subregion_eu_ad),
  AM(R.string.subregion_eu_am),
  AT(R.string.subregion_eu_at),
  BY(R.string.subregion_eu_by),
  BE(R.string.subregion_eu_be),
  BA(R.string.subregion_eu_ba),
  BG(R.string.subregion_eu_bg),
  HR(R.string.subregion_eu_hr),
  CY(R.string.subregion_eu_cy),
  CZ(R.string.subregion_eu_cz),
  DK(R.string.subregion_eu_dk),
  EE(R.string.subregion_eu_ee),
  FO(R.string.subregion_eu_fo),
  FI(R.string.subregion_eu_fi),
  FR(R.string.subregion_eu_fr),
  GE(R.string.subregion_eu_ge),
  DE(R.string.subregion_eu_de),
  GI(R.string.subregion_eu_gi),
  GR(R.string.subregion_eu_gr),
  HU(R.string.subregion_eu_hu),
  IS(R.string.subregion_eu_is),
  IE(R.string.subregion_eu_ie),
  IM(R.string.subregion_eu_im),
  IT(R.string.subregion_eu_it),
  XK(R.string.subregion_eu_xk),
  LV(R.string.subregion_eu_lv),
  LI(R.string.subregion_eu_li),
  LT(R.string.subregion_eu_lt),
  LU(R.string.subregion_eu_lu),
  MK(R.string.subregion_eu_mk),
  MT(R.string.subregion_eu_mt),
  MD(R.string.subregion_eu_md),
  MC(R.string.subregion_eu_mc),
  ME(R.string.subregion_eu_me),
  NL(R.string.subregion_eu_nl),
  NO(R.string.subregion_eu_no),
  PO(R.string.subregion_eu_po),
  PT(R.string.subregion_eu_pt),
  RO(R.string.subregion_eu_ro),
  RU(R.string.subregion_eu_ru),
  SM(R.string.subregion_eu_sm),
  RS(R.string.subregion_eu_rs),
  SK(R.string.subregion_eu_sk),
  SI(R.string.subregion_eu_si),
  ES(R.string.subregion_eu_es),
  SE(R.string.subregion_eu_se),
  CH(R.string.subregion_eu_ch),
  TR(R.string.subregion_eu_tr),
  UA(R.string.subregion_eu_ua),
  UK(R.string.subregion_eu_uk),
  VA(R.string.subregion_eu_va),
  ;

  override val subregionCode: String = name
}

enum class UsSubregion(
  @StringRes override val nameResId: Int,
  override val isAllSubregions: Boolean = false
) : Subregion {
  ALL(R.string.subregion_us_all, isAllSubregions = true),
  AL(R.string.subregion_us_al),
  AK(R.string.subregion_us_ak),
  AS(R.string.subregion_us_as),
  AZ(R.string.subregion_us_az),
  AR(R.string.subregion_us_ar),
  CA(R.string.subregion_us_ca),
  CO(R.string.subregion_us_co),
  CT(R.string.subregion_us_ct),
  DE(R.string.subregion_us_de),
  DC(R.string.subregion_us_dc),
  FL(R.string.subregion_us_fl),
  GA(R.string.subregion_us_ga),
  GU(R.string.subregion_us_gu),
  HI(R.string.subregion_us_hi),
  ID(R.string.subregion_us_id),
  IL(R.string.subregion_us_il),
  IN(R.string.subregion_us_in),
  IA(R.string.subregion_us_ia),
  KS(R.string.subregion_us_ks),
  KY(R.string.subregion_us_ky),
  LA(R.string.subregion_us_la),
  ME(R.string.subregion_us_me),
  MD(R.string.subregion_us_md),
  MA(R.string.subregion_us_ma),
  MI(R.string.subregion_us_mi),
  MN(R.string.subregion_us_mn),
  MS(R.string.subregion_us_ms),
  MO(R.string.subregion_us_mo),
  MT(R.string.subregion_us_mt),
  NE(R.string.subregion_us_ne),
  NV(R.string.subregion_us_nv),
  NH(R.string.subregion_us_nh),
  NJ(R.string.subregion_us_nj),
  NM(R.string.subregion_us_nm),
  NY(R.string.subregion_us_ny),
  NC(R.string.subregion_us_nc),
  ND(R.string.subregion_us_nd),
  MP(R.string.subregion_us_mp),
  OH(R.string.subregion_us_oh),
  OK(R.string.subregion_us_ok),
  OR(R.string.subregion_us_or),
  PA(R.string.subregion_us_pa),
  PR(R.string.subregion_us_pr),
  RI(R.string.subregion_us_ri),
  SC(R.string.subregion_us_sc),
  SD(R.string.subregion_us_sd),
  TN(R.string.subregion_us_tn),
  TX(R.string.subregion_us_tx),
  VI(R.string.subregion_us_vi),
  UM(R.string.subregion_us_um),
  UT(R.string.subregion_us_ut),
  VT(R.string.subregion_us_vt),
  VA(R.string.subregion_us_va),
  WA(R.string.subregion_us_wa),
  WV(R.string.subregion_us_wv),
  WI(R.string.subregion_us_wi),
  WY(R.string.subregion_us_wy),
  ;

  override val subregionCode: String = name
}
