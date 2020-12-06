package com.aradipatrik.claptrap.feature.transactions.list.model

import com.aradipatrik.claptrap.domain.Category
import org.joda.time.DateTime

sealed class TransactionsViewEvent {
  object ActionClick : TransactionsViewEvent()
  object BackClick : TransactionsViewEvent()
  data class TransactionTypeSwitch(val newType: TransactionType) : TransactionsViewEvent()
  object MonthSelectorClick : TransactionsViewEvent()

  sealed class AddTransactionViewEvent : TransactionsViewEvent() {
    data class MemoChange(val memo: String) : AddTransactionViewEvent()
    data class CategorySelected(val category: Category) : AddTransactionViewEvent()
    object CalendarClick : AddTransactionViewEvent()
    data class DateSelected(val date: DateTime) : AddTransactionViewEvent()

    sealed class CalculatorEvent : AddTransactionViewEvent() {
      data class NumberClick(val number: Int) : CalculatorEvent()
      object PointClick : CalculatorEvent()
      object DeleteOneClick : CalculatorEvent()
      object PlusClick : CalculatorEvent()
      object MinusClick : CalculatorEvent()
      object NumberPadActionClick : CalculatorEvent()
    }
  }

}
