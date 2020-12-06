package com.aradipatrik.claptrap.feature.transactions.list.model

import org.joda.time.DateTime

sealed class TransactionsViewEffect {
  object ShowAddTransactionMenu : TransactionsViewEffect()
  object HideTransactionMenu : TransactionsViewEffect()
  object PlayAddAnimation : TransactionsViewEffect()
  object PlayReverseAddAnimation : TransactionsViewEffect()

  object MorphCheckToEquals : TransactionsViewEffect()
  object MorphEqualsToCheck : TransactionsViewEffect()

  object Back : TransactionsViewEffect()

  object ShowYearMontSelector : TransactionsViewEffect()
  object HideYearMonthSelector : TransactionsViewEffect()

  data class ShowDatePickerAt(val date: DateTime) : TransactionsViewEffect()
}
