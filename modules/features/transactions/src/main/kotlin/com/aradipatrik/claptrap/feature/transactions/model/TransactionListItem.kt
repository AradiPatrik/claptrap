package com.aradipatrik.claptrap.feature.transactions.model

sealed class TransactionListItem {
  data class Header(val title: String) : TransactionListItem()
  data class Item(val transactionPresentation: TransactionPresentation) : TransactionListItem()
}
