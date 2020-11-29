package com.aradipatrik.claptrap.feature.transactions.list.model

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.interactors.interfaces.todo.TransactionInteractor
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TransactionsViewModel @ViewModelInject constructor(
  transactionInteractor: TransactionInteractor
) : ClaptrapViewModel<TransactionsViewState, TransactionsViewEvent, TransactionsViewEffect>(
  TransactionsViewState.Loading
) {
  private var loadedTransactions = emptyList<Transaction>()

  init {
    transactionInteractor.getAllTransactions()
      .onEach(::setLoadedTransactions)
      .launchIn(viewModelScope)
  }

  private fun setLoadedTransactions(transactions: List<Transaction>) = setState {
    loadedTransactions = transactions
    TransactionsViewState.TransactionsLoaded(
      transactions = transactions,
      refreshing = false
    )
  }

  override fun processInput(viewEvent: TransactionsViewEvent) = when(viewEvent) {
    is TransactionsViewEvent.AddClick -> setState {
      viewEffects.send(TransactionsViewEffect.ShowAddTransactionMenu)
      viewEffects.send(TransactionsViewEffect.PlayAddAnimation)
      TransactionsViewState.Adding
    }
    TransactionsViewEvent.BackClick -> setState {
      viewEffects.send(TransactionsViewEffect.PlayReverseAddAnimation)
      viewEffects.send(TransactionsViewEffect.HiedTransactionMenu)
      TransactionsViewState.TransactionsLoaded(
        transactions = loadedTransactions,
        refreshing = true
      )
    }
    is TransactionsViewEvent.TransactionTypeSwitch -> TODO()
  }
}
