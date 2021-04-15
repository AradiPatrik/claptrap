package com.aradipatrik.claptrap.domainnetworkmappers

import com.aradipatrik.claptrap.apimodels.CategoryWire
import com.aradipatrik.claptrap.domain.Category

object CategoryMapper {
  @JvmName("toWireInstance")
  fun Category.toWire() = CategoryWire(
    id = id,
    name = name,
    icon = icon.name
  )

  fun toWire(category: Category) = category.toWire()
}
