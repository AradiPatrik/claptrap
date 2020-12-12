package com.aradipatrik.claptrap.feature.transactions.edit.model

sealed class EditTransactionViewState {
  data class Placeholder(val transactionId: String) : EditTransactionViewState()
}