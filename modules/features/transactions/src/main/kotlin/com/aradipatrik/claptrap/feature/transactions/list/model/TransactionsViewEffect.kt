package com.aradipatrik.claptrap.feature.transactions.list.model

import com.aradipatrik.claptrap.feature.transactions.databinding.ListItemTransactionItemBinding
import org.joda.time.DateTime

sealed class TransactionsViewEffect {
  object ToggleNumberPadAction : TransactionsViewEffect()
  object Back : TransactionsViewEffect()
  data class ShowDatePickerAt(val date: DateTime) : TransactionsViewEffect()
  data class ScrollToTransaction(val transactionId: String) : TransactionsViewEffect()
  data class NavigateToEditTransaction(
    val itemView: ListItemTransactionItemBinding,
    val transactionId: String
  ) : TransactionsViewEffect()
}
