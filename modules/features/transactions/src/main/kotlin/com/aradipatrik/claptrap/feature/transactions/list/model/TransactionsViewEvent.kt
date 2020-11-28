package com.aradipatrik.claptrap.feature.transactions.list.model

sealed class TransactionsViewEvent {
  object AddClick : TransactionsViewEvent()
  object BackClick : TransactionsViewEvent()
  data class TransactionTypeSwitch(val newType: TransactionType) : TransactionsViewEvent()
}
