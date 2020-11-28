package com.aradipatrik.claptrap.feature.transactions.list.model

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.interactors.interfaces.todo.TransactionInteractor
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TransactionsViewModel @ViewModelInject constructor(
  transactionInteractor: TransactionInteractor
) : ClaptrapViewModel<TransactionsViewState, TransactionsViewEvent, TransactionsViewEffect>(
  TransactionsViewState.Loading
) {
  init {
    transactionInteractor.getAllTransactions()
      .onEach(::setLoadedTransactions)
      .launchIn(viewModelScope)
  }

  private fun setLoadedTransactions(transactions: List<Transaction>) = setState {
    TransactionsViewState.TransactionsLoaded(
      transactions = transactions,
      refreshing = false
    )
  }

  override fun processInput(viewEvent: TransactionsViewEvent) = when(viewEvent) {
    is TransactionsViewEvent.AddClick -> setState {
      viewEffects.send(TransactionsViewEffect.ShowAddTransactionMenu)
      TransactionsViewState.Adding
    }
    TransactionsViewEvent.BackClick -> sideEffect {
      viewEffects.send(TransactionsViewEffect.HiedTransactionMenu)
    }
    is TransactionsViewEvent.TransactionTypeSwitch -> TODO()
  }
}
