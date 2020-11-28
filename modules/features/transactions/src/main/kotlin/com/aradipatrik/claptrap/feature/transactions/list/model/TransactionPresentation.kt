package com.aradipatrik.claptrap.feature.transactions.list.model

import androidx.annotation.DrawableRes
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
        categoryIcon = R.drawable.ic_money,
        note = transaction.note,
        currencySymbol = it.money.currencyUnit.symbol
      )
    }
  }

  val monthAsText = domain.date.monthOfYear().asText
}
