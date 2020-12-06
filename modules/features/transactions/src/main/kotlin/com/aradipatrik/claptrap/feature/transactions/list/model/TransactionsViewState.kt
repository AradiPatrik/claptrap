package com.aradipatrik.claptrap.feature.transactions.list.model

import com.aradipatrik.claptrap.domain.Category
import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.feature.transactions.list.model.calculator.CalculatorState
import com.aradipatrik.claptrap.feature.transactions.list.model.calculator.NumberOnCalculator
import org.joda.time.DateTime

sealed class TransactionsViewState {
  object Loading : TransactionsViewState()

  data class TransactionsLoaded(
    val transactions: List<Transaction>,
    val refreshing: Boolean
  ) : TransactionsViewState()

  data class Adding(
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val categories: List<Category> = emptyList(),
    val date: DateTime = DateTime.now(),
    val selectedCategory: Category? = null,
    val memo: String = "",
    val calculatorState: CalculatorState = CalculatorState.SingleValue(NumberOnCalculator("0"))
  ) : TransactionsViewState()
}
