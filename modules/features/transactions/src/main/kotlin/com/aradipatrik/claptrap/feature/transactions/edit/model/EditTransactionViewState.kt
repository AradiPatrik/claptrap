package com.aradipatrik.claptrap.feature.transactions.edit.model

import com.aradipatrik.claptrap.domain.Transaction

sealed class EditTransactionViewState {
  data class Editing(
    val transaction: Transaction? = null
  ) : EditTransactionViewState()
}