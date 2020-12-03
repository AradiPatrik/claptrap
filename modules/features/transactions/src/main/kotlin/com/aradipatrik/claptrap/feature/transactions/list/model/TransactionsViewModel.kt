package com.aradipatrik.claptrap.feature.transactions.list.model

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEffect.*
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent.*
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent.AddTransactionViewEvent.CalculatorEvent
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent.AddTransactionViewEvent.MemoChange
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewState.*
import com.aradipatrik.claptrap.feature.transactions.list.model.calculator.CalculatorStateReducer
import com.aradipatrik.claptrap.interactors.interfaces.todo.TransactionInteractor
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TransactionsViewModel @ViewModelInject constructor(
  transactionInteractor: TransactionInteractor
) : ClaptrapViewModel<TransactionsViewState, TransactionsViewEvent, TransactionsViewEffect>(
  Loading
) {
  private var loadedTransactions = emptyList<Transaction>()

  init {
    transactionInteractor.getAllTransactions()
      .onEach(::setLoadedTransactions)
      .launchIn(viewModelScope)
  }

  private fun setLoadedTransactions(transactions: List<Transaction>) = reduceState { state ->
    loadedTransactions = transactions

    if (state is Loading || state is TransactionsLoaded) {
      TransactionsLoaded(
        transactions = transactions,
        refreshing = false
      )
    } else {
      state
    }
  }

  override fun processInput(viewEvent: TransactionsViewEvent) = when(viewEvent) {
    is ActionClick -> setState {
      viewEffects.send(ShowAddTransactionMenu)
      viewEffects.send(PlayAddAnimation)
      Adding(transactionType = TransactionType.EXPENSE)
    }
    BackClick -> reduceState { state ->
      if (state is Adding) {
        viewEffects.send(PlayReverseAddAnimation)
        viewEffects.send(HiedTransactionMenu)
        TransactionsLoaded(
          transactions = loadedTransactions,
          refreshing = true
        )
      } else {
        state.also { viewEffects.send(Back) }
      }
    }
    is TransactionTypeSwitch -> reduceSpecificState<Adding> {
      it.copy(transactionType = viewEvent.newType)
    }
    is CalculatorEvent -> reduceSpecificState<Adding> {
      it.copy(calculatorState = CalculatorStateReducer.reduceState(it.calculatorState, viewEvent))
    }
    is MemoChange -> error("TODO")
  }
}
