package com.aradipatrik.claptrap.feature.transactions.ui

import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.feature.transactions.model.TransactionListItem
import com.aradipatrik.claptrap.feature.transactions.model.TransactionPresentation
import javax.inject.Inject

class TransactionListBuilderDelegate @Inject constructor() {
  fun generateListItemsFrom(transactions: List<Transaction>) = transactions
    .sortedByDescending(Transaction::date)
    .groupBy { it.date.monthOfYear }
    .flatMap { (_, transactions) ->
      mutableListOf<TransactionListItem>().apply {
        add(TransactionListItem.Header(transactions.first().date.monthOfYear().asText))
        addAll(
          transactions
            .map(TransactionPresentation::fromTransaction)
            .map { TransactionListItem.Item(it) }
        )
      }
    }
    .drop(1)
}
