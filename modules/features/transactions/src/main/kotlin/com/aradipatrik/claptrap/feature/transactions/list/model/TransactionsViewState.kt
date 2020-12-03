package com.aradipatrik.claptrap.feature.transactions.list.model

import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.feature.transactions.list.model.calculator.CalculatorState
import com.aradipatrik.claptrap.feature.transactions.list.model.calculator.NumberOnCalculator

sealed class TransactionsViewState {
  object Loading : TransactionsViewState()

  data class TransactionsLoaded(
    val transactions: List<Transaction>,
    val refreshing: Boolean
  ) : TransactionsViewState()

  data class Adding(
    val transactionType: TransactionType,
    val calculatorState: CalculatorState = CalculatorState.SingleValue(NumberOnCalculator("0"))
  ) : TransactionsViewState()
}
