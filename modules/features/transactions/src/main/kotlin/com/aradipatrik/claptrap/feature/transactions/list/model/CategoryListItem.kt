package com.aradipatrik.claptrap.feature.transactions.list.model

import com.aradipatrik.claptrap.domain.Category

data class CategoryListItem(
  val category: Category,
  val isSelected: Boolean
)
