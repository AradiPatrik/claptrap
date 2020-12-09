package com.aradipatrik.claptrap.fakeinteractors.transaction

import com.aradipatrik.claptrap.fakeinteractors.generators.CommonMockGenerator.of
import com.aradipatrik.claptrap.fakeinteractors.generators.TransactionMockGenerator.nextTransactionInYearMonth
import com.aradipatrik.claptrap.interactors.interfaces.todo.TransactionInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.joda.time.YearMonth
import javax.inject.Inject
import kotlin.random.Random

class TransactionInteractorFake @Inject constructor() : TransactionInteractor {
  private val transactions = MutableStateFlow(mapOf(YearMonth.now() to initialTransactions))

  override fun getAllTransactions() = transactions
    .map { it.values.flatten() }

  override fun getAllTransactionsInYearMonth(yearMonth: YearMonth) = transactions
    .map { it.getValue(yearMonth) }
    .also { populateTransactionsWithYearMonthIfMissing(yearMonth) }

  private fun populateTransactionsWithYearMonthIfMissing(yearMonth: YearMonth) {
    if (!transactions.value.containsKey(yearMonth)) {
      transactions.value = transactions.value +
        (yearMonth to (100 of { Random.nextTransactionInYearMonth(yearMonth) }))
    }
  }
}

internal val initialTransactions = 100 of { Random.nextTransactionInYearMonth(YearMonth.now()) }
