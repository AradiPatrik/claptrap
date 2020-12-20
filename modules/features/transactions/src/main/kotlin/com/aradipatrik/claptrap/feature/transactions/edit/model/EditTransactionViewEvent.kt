package com.aradipatrik.claptrap.feature.transactions.edit.model

sealed class EditTransactionViewEvent {
  object BackClick : EditTransactionViewEvent()
  object DeleteButtonClick : EditTransactionViewEvent()
  object EditDoneClick : EditTransactionViewEvent()
  object CategorySelectorClick : EditTransactionViewEvent()
  object ScrimClick : EditTransactionViewEvent()
  data class MemoChange(val memo: String) : EditTransactionViewEvent()
  data class AmountChange(val amount: String) : EditTransactionViewEvent()
}
