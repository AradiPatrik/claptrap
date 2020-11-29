package com.aradipatrik.claptrap.feature.transactions.list.model

import com.aradipatrik.claptrap.domain.Transaction

sealed class TransactionsViewState {
  object Loading : TransactionsViewState()

  data class TransactionsLoaded(
    val transactions: List<Transaction>,
    val refreshing: Boolean
  ) : TransactionsViewState()

  data class Adding(
    val transactionType: TransactionType
  ) : TransactionsViewState()
}
