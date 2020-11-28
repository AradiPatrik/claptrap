package com.aradipatrik.claptrap.feature.transactions.list.model

sealed class TransactionsViewEffect {
  object ShowAddTransactionMenu : TransactionsViewEffect()
  object HiedTransactionMenu : TransactionsViewEffect()
}
