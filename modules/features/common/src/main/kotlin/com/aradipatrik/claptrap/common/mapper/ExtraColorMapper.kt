package com.aradipatrik.claptrap.common.mapper

import com.aradipatrik.claptrap.domain.ExtraColor
import com.aradipatrik.claptrap.domain.ExtraColor.*
import com.aradipatrik.claptrap.theme.R

object ExtraColorMapper {
  private val extraColorToAttributeIds = hashMapOf(
    BLUE to R.attr.extraColorBlue,
    AMBER to R.attr.extraColorAmber,
    PURPLE to R.attr.extraColorPurple,
    LIME to R.attr.extraColorLime,
    LIGHT_GREEN to R.attr.extraColorLightGreen,
    GREEN to R.attr.extraColorGreen,
    TEAL to R.attr.extraColorTeal,
    CYAN to R.attr.extraColorCyan,
    PINK to R.attr.extraColorPink,
    DEEP_PURPLE to R.attr.extraColorDeepPurple
  )

  val ExtraColor.asColorAttribute get() = extraColorToAttributeIds[this]
    ?: error("Non existent color attribute: $this")
}
