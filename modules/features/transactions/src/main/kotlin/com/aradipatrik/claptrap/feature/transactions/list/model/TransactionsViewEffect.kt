package com.aradipatrik.claptrap.feature.transactions.list.model

sealed class TransactionsViewEffect {
  object ShowAddTransactionMenu : TransactionsViewEffect()
  object HideTransactionMenu : TransactionsViewEffect()
  object PlayAddAnimation : TransactionsViewEffect()
  object PlayReverseAddAnimation : TransactionsViewEffect()

  object MorphCheckToEquals : TransactionsViewEffect()
  object MorphEqualsToCheck : TransactionsViewEffect()

  object Back : TransactionsViewEffect()
}
