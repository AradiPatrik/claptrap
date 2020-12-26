package com.aradipatrik.claptrap.common.mapper

import com.aradipatrik.claptrap.domain.StatColor
import com.aradipatrik.claptrap.domain.StatColor.*
import com.aradipatrik.claptrap.theme.R

object StatColorMapper {
  private val statColorIdsToAttributeIds = hashMapOf(
    STAT_COLOR_ONE to R.attr.statColorOne,
    STAT_COLOR_TWO to R.attr.statColorTwo,
    STAT_COLOR_THREE to R.attr.statColorThree,
    STAT_COLOR_FOUR to R.attr.statColorFour,
    STAT_COLOR_FIVE to R.attr.statColorFive,
    STAT_COLOR_SIX to R.attr.statColorSix,
    STAT_COLOR_SEVEN to R.attr.statColorSeven,
    STAT_COLOR_EIGHT to R.attr.statColorEight,
    STAT_COLOR_NINE to R.attr.statColorNine,
    STAT_COLOR_TEN to R.attr.statColorTen
  )

  val StatColor.asColorAttribute get() = statColorIdsToAttributeIds[this]
    ?: error("Non existent color attribute: $this")
}
