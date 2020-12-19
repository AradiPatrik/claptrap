package com.aradipatrik.claptrap.feature.transactions.edit.model

import org.joda.money.Money

sealed class EditTransactionViewEvent {
  object BackClick : EditTransactionViewEvent()
  object DeleteButtonClick : EditTransactionViewEvent()
  object EditDoneClick : EditTransactionViewEvent()
  data class MemoChange(val memo: String) : EditTransactionViewEvent()
  data class AmountChange(val amount: String) : EditTransactionViewEvent()
}