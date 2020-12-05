package com.aradipatrik.claptrap.feature.transactions.list.model

import com.aradipatrik.claptrap.domain.CategoryIcon
import com.aradipatrik.claptrap.feature.transactions.R

object CategoryIconMapper {
  val CategoryIcon.drawableRes get() = when(this) {
    CategoryIcon.CAR -> R.drawable.category_icon_car
    CategoryIcon.CART -> R.drawable.category_icon_cart
    CategoryIcon.HEALTH -> R.drawable.category_icon_health
    CategoryIcon.HOME -> R.drawable.category_icon_home
    CategoryIcon.FOOD -> R.drawable.category_icon_pizza_slice
    CategoryIcon.SALARY -> R.drawable.category_icon_salary
    CategoryIcon.SOCIAL -> R.drawable.category_icon_social
    CategoryIcon.TRANSPORTATION -> R.drawable.category_icon_train
    CategoryIcon.WORK -> R.drawable.category_icon_work
    CategoryIcon.WORKOUT -> R.drawable.category_icon_workout
  }
}