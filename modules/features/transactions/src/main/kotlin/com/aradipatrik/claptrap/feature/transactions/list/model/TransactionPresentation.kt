package com.aradipatrik.claptrap.feature.transactions.list.model

import androidx.annotation.DrawableRes
import com.aradipatrik.claptrap.domain.Category
import com.aradipatrik.claptrap.domain.CategoryIcon
import com.aradipatrik.claptrap.domain.CategoryIcon.*
import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.feature.transactions.R
import java.util.*

data class TransactionPresentation(
  val domain: Transaction,
  val amount: String,
  val date: String,
  @DrawableRes val categoryIcon: Int,
  val note: String,
  val currencySymbol: String
) {
  companion object {
    fun fromTransaction(transaction: Transaction) = transaction.let {
      TransactionPresentation(
        domain = it,
        amount = it.money.amount.toString(),
        date = it.date.toString("YYYY / MM / dd", Locale.getDefault()),
        categoryIcon = transaction.category.icon.drawableRes,
        note = transaction.note,
        currencySymbol = it.money.currencyUnit.symbol
      )
    }

    private val CategoryIcon.drawableRes get() = when(this) {
      CAR -> R.drawable.category_icon_car
      CART -> R.drawable.category_icon_cart
      HEALTH -> R.drawable.category_icon_health
      HOME -> R.drawable.category_icon_home
      FOOD -> R.drawable.category_icon_pizza_slice
      SALARY -> R.drawable.category_icon_salary
      SOCIAL -> R.drawable.category_icon_social
      TRANSPORTATION -> R.drawable.category_icon_train
      WORK -> R.drawable.category_icon_work
      WORKOUT -> R.drawable.category_icon_workout
    }
  }

  val monthAsText: String = domain.date.monthOfYear().asText
}
