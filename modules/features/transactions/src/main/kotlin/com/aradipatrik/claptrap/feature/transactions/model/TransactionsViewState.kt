package com.aradipatrik.claptrap.feature.transactions.model

import com.aradipatrik.claptrap.domain.Transaction

sealed class TransactionsViewState {
  object Loading : TransactionsViewState()

  data class Loaded(
    val transactions: List<Transaction>,
    val refreshing: Boolean
  ) : TransactionsViewState()
}
