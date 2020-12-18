package com.aradipatrik.claptrap.feature.transactions.edit.model

sealed class EditTransactionViewEvent {
  object BackClick : EditTransactionViewEvent()
  object DeleteClick : EditTransactionViewEvent()
}