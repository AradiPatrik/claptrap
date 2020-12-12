package com.aradipatrik.claptrap.feature.transactions.edit.model

import androidx.hilt.lifecycle.ViewModelInject
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewState.Placeholder
import com.aradipatrik.claptrap.interactors.interfaces.todo.TransactionInteractor
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel

class EditTransactionViewModel @ViewModelInject constructor(
  val transactionInteractor: TransactionInteractor
) : ClaptrapViewModel<EditTransactionViewState,
  EditTransactionViewEvent,
  EditTransactionViewEffect>(Placeholder) {
  override fun processInput(viewEvent: EditTransactionViewEvent) {
    TODO("Not yet implemented")
  }
}