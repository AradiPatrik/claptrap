package com.aradipatrik.claptrap.feature.transactions.list.model

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.aradipatrik.claptrap.domain.Category
import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEffect.*
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent.*
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent.AddTransactionViewEvent.*
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent.AddTransactionViewEvent.CalculatorEvent.DeleteOneClick
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent.AddTransactionViewEvent.CalculatorEvent.NumberPadActionClick
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewState.*
import com.aradipatrik.claptrap.feature.transactions.list.model.calculator.BinaryOperation
import com.aradipatrik.claptrap.feature.transactions.list.model.calculator.CalculatorState
import com.aradipatrik.claptrap.feature.transactions.list.model.calculator.CalculatorStateReducer
import com.aradipatrik.claptrap.interactors.interfaces.todo.CategoryInteractor
import com.aradipatrik.claptrap.interactors.interfaces.todo.TransactionInteractor
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import org.joda.time.DateTime

class TransactionsViewModel @ViewModelInject constructor(
  transactionInteractor: TransactionInteractor,
  private val categoryInteractor: CategoryInteractor
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
    is ActionClick -> goToAddTransaction()
    is BackClick -> goBack()
    is TransactionTypeSwitch -> switchTransactionType(viewEvent)
    is CalculatorEvent -> handleCalculatorEvent(viewEvent)
    is CategorySelected -> selectCategory(viewEvent.category)
    is MemoChange -> changeMemo(viewEvent.memo)
    is CalendarClick -> showDatePicker()
    is DateSelected -> setDate(viewEvent.date)
    is MonthSelectorClick -> showMonthSelector()
  }

  private fun showMonthSelector() = sideEffect {
    viewEffects.send(ShowMonthSelectorMenu)
  }

  private fun setDate(date: DateTime) = reduceSpecificState<Adding> { state ->
    state.copy(date = date)
  }

  private fun showDatePicker() = withState<Adding> { state ->
    viewEffects.send(ShowDatePickerAt(state.date))
  }

  private fun changeMemo(newMemo: String) = reduceSpecificState<Adding> { state ->
    state.copy(memo = newMemo)
  }

  private fun selectCategory(category: Category) = reduceSpecificState<Adding> { state ->
    state.copy(selectedCategory = category)
  }

  private fun handleCalculatorEvent(
    viewEvent: CalculatorEvent
  ) = reduceSpecificState<Adding> { state ->
    if (wereAddTransactionClicked(state, viewEvent)) {
      handleAddTransaction()
    } else {
      handleNumberPadClick(state, viewEvent)
    }
  }

  private suspend fun handleAddTransaction(): TransactionsLoaded {
    viewEffects.send(PlayReverseAddAnimation)
    viewEffects.send(HideTransactionMenu)
    return TransactionsLoaded(
      transactions = loadedTransactions,
      refreshing = false
    )
  }

  private suspend fun handleNumberPadClick(
    state: Adding,
    viewEvent: CalculatorEvent
  ): Adding = state.copy(
    calculatorState = CalculatorStateReducer.reduceState(state.calculatorState, viewEvent)
  ).also { newState ->
    if (viewEvent is NumberPadActionClick) viewEffects.send(MorphEqualsToCheck)
    if (isOperatorAdded(state, viewEvent)) viewEffects.send(MorphCheckToEquals)
    if (isOperatorDeleted(state, newState, viewEvent)) viewEffects.send(MorphCheckToEquals)
  }

  private fun isOperatorAdded(
    state: Adding,
    viewEvent: CalculatorEvent
  ) = (state.calculatorState !is BinaryOperation
    && (viewEvent is CalculatorEvent.MinusClick || viewEvent is CalculatorEvent.PlusClick))

  private fun isOperatorDeleted(
    state: Adding,
    newState: Adding,
    viewEvent: CalculatorEvent
  ) = state.calculatorState is BinaryOperation &&
    viewEvent is DeleteOneClick &&
    newState.calculatorState is CalculatorState.SingleValue

  private fun wereAddTransactionClicked(
    state: Adding,
    viewEvent: CalculatorEvent
  ) = state.calculatorState is CalculatorState.SingleValue && viewEvent is NumberPadActionClick

  private fun switchTransactionType(viewEvent: TransactionTypeSwitch) {
    reduceSpecificState<Adding> {
      it.copy(transactionType = viewEvent.newType)
    }
  }

  private fun goBack() = reduceState { state ->
    if (state is Adding) {
      viewEffects.send(PlayReverseAddAnimation)
      viewEffects.send(HideTransactionMenu)
      TransactionsLoaded(
        transactions = loadedTransactions,
        refreshing = true
      )
    } else {
      state.also { viewEffects.send(Back) }
    }
  }

  private fun goToAddTransaction() = setState {
    viewEffects.send(ShowAddTransactionMenu)
    viewEffects.send(PlayAddAnimation)
    reduceSpecificState<Adding> { state ->
      val categories = categoryInteractor.getAllCategories().take(1).single()
      state.copy(
        categories = categories,
        selectedCategory = categories.first()
      )
    }
    Adding(TransactionType.EXPENSE)
  }
}
