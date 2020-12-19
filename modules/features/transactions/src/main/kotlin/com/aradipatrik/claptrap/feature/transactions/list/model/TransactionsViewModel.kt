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
import com.aradipatrik.claptrap.mvi.MviUtil.ignore
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.joda.time.DateTime
import org.joda.time.YearMonth
import java.util.*

class TransactionsViewModel @ViewModelInject constructor(
  private val transactionInteractor: TransactionInteractor,
  private val categoryInteractor: CategoryInteractor
) : ClaptrapViewModel<TransactionsViewState, TransactionsViewEvent, TransactionsViewEffect>(
  Loading
) {
  private var getTransactionsInCurrentYearMonthJob =
    listenToTransactionsOfYearMonth(YearMonth.now())

  private fun setLoadedTransactions(transactions: List<Transaction>) = reduceState { state ->
    when (state) {
      is Loading -> TransactionsLoaded(transactions = transactions, refreshing = false)
      is TransactionsLoaded -> state.copy(transactions = transactions)
      else -> state
    }
  }

  private fun listenToTransactionsOfYearMonth(yearMonth: YearMonth) = transactionInteractor
    .getAllTransactionsInYearMonthFlow(yearMonth)
    .onEach(::setLoadedTransactions)
    .launchIn(viewModelScope)

  override fun processInput(viewEvent: TransactionsViewEvent) = when (viewEvent) {
    is ActionClick -> goToAddTransaction()
    is BackClick -> goBack()
    is TransactionTypeSwitch -> switchTransactionType(viewEvent)
    is CalculatorEvent -> addTransactionOrAppendToNumberDisplay(viewEvent)
    is CategorySelected -> selectCategory(viewEvent.category)
    is MemoChange -> changeMemo(viewEvent.memo)
    is CalendarClick -> showDatePicker()
    is DateSelected -> setDate(viewEvent.date)
    is YearMonthSelectorClick -> toggleYearMonthSelector()
    is MonthSelected -> selectYearMonth(viewEvent.month)
    is YearIncreased -> increaseYear()
    is YearDecreased -> decreaseYear()
    is TransactionItemClicked -> goToEditTransaction(viewEvent.transactionId)
  }

  private fun goToEditTransaction(transactionId: String) = viewModelScope.launch {
    viewEffects.emit(NavigateToEditTransaction(transactionId))
  }.ignore()

  private fun decreaseYear() = reduceSpecificState<TransactionsLoaded> { state ->
    val newYearMonth = state.yearMonth.withYear(state.yearMonth.year - 1)
    startListeningToNewYearMonth(newYearMonth)
    state.copy(yearMonth = newYearMonth)
  }

  private fun increaseYear() = reduceSpecificState<TransactionsLoaded> { state ->
    val newYearMonth = state.yearMonth.withYear(state.yearMonth.year + 1)
    startListeningToNewYearMonth(newYearMonth)
    state.copy(yearMonth = newYearMonth)
  }

  private fun selectYearMonth(month: Int) = reduceSpecificState<TransactionsLoaded> { state ->
    val newYearMonth = state.yearMonth.withMonthOfYear(month)
    startListeningToNewYearMonth(newYearMonth)
    state.copy(yearMonth = newYearMonth)
  }

  private fun startListeningToNewYearMonth(newYearMonth: YearMonth) {
    getTransactionsInCurrentYearMonthJob.cancel()
    getTransactionsInCurrentYearMonthJob = listenToTransactionsOfYearMonth(newYearMonth)
  }

  private fun toggleYearMonthSelector() = reduceSpecificState<TransactionsLoaded> { state ->
    state.copy(isYearMonthSelectorOpen = !state.isYearMonthSelectorOpen)
  }

  private fun setDate(date: DateTime) = reduceSpecificState<Adding> { state ->
    state.copy(date = date)
  }

  private fun showDatePicker() = withState<Adding> { state ->
    viewEffects.emit(ShowDatePickerAt(state.date))
  }

  private fun changeMemo(newMemo: String) = reduceSpecificState<Adding> { state ->
    state.copy(memo = newMemo)
  }

  private fun selectCategory(category: Category) = reduceSpecificState<Adding> { state ->
    state.copy(selectedCategory = category)
  }

  private fun addTransactionOrAppendToNumberDisplay(
    viewEvent: CalculatorEvent
  ) = reduceSpecificState<Adding> { state ->
    if (wereAddTransactionClicked(state, viewEvent)) {
      addTransactionOfState(state)
    } else {
      addNumberOrOperatorToState(state, viewEvent)
    }
  }

  private fun addTransactionOfState(state: Adding): TransactionsLoaded {
    saveTransaction(createTransactionFromAddingState(state))
    return TransactionsLoaded(
      transactions = state.oldTransactions,
      yearMonth = state.transactionsYearMonth,
      refreshing = false
    )
  }

  private fun saveTransaction(newTransaction: Transaction) = sideEffect {
    viewEffects.emit(ScrollToTransaction(newTransaction.id))
    transactionInteractor.saveTransaction(newTransaction)
  }

  private suspend fun addNumberOrOperatorToState(
    state: Adding,
    viewEvent: CalculatorEvent
  ): Adding = state.copy(
    calculatorState = CalculatorStateReducer.reduceState(state.calculatorState, viewEvent)
  ).also { newState ->
    if (viewEvent is NumberPadActionClick) viewEffects.emit(ToggleNumberPadAction)
    if (isOperatorAdded(state, viewEvent)) viewEffects.emit(ToggleNumberPadAction)
    if (isOperatorDeleted(state, newState, viewEvent)) viewEffects.emit(ToggleNumberPadAction)
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
      TransactionsLoaded(
        transactions = state.oldTransactions,
        yearMonth = state.transactionsYearMonth,
        refreshing = true
      )
    } else if (state is TransactionsLoaded && state.isYearMonthSelectorOpen) {
      state.copy(isYearMonthSelectorOpen = false)
    } else {
      state.also { viewEffects.emit(Back) }
    }
  }

  private fun goToAddTransaction() = reduceSpecificState<TransactionsLoaded> { oldState ->
    reduceSpecificState<Adding> { state ->
      val categories = categoryInteractor.getAllCategories().take(1).single()
      state.copy(
        categories = categories,
        selectedCategory = categories.first()
      )
    }
    Adding(
      TransactionType.EXPENSE,
      transactionsYearMonth = oldState.yearMonth,
      oldTransactions = oldState.transactions
    )
  }
}

private fun createTransactionFromAddingState(state: Adding): Transaction {
  require(state.selectedCategory != null) {
    "Add should not be called without a selected category"
  }

  return Transaction(
    id = UUID.randomUUID().toString(),
    money = Money.of(CurrencyUnit.USD, state.calculatorState.value.asBigDecimal),
    memo = state.memo,
    date = state.date,
    category = state.selectedCategory
  )
}
