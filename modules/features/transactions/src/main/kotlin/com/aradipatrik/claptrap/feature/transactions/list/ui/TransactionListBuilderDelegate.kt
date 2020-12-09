package com.aradipatrik.claptrap.feature.transactions.list.ui

import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionListItem
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionPresentation
import javax.inject.Inject

class TransactionListBuilderDelegate @Inject constructor() {
  fun generateListItemsFrom(transactions: List<Transaction>) = transactions
    .sortedByDescending(Transaction::date)
    .groupBy { it.date.dayOfMonth }
    .flatMap { (_, transactions) ->
      mutableListOf<TransactionListItem>().apply {
        add(TransactionListItem.Header(transactions.first().date.toString("MMMM dd")))
        addAll(
          transactions
            .map(TransactionPresentation::fromTransaction)
            .map { TransactionListItem.Item(it) }
        )
      }
    }
    .drop(1)
}
