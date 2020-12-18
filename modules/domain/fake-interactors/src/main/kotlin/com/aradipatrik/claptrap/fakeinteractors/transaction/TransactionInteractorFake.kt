package com.aradipatrik.claptrap.fakeinteractors.transaction

import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.fakeinteractors.generators.CommonMockGenerator.of
import com.aradipatrik.claptrap.fakeinteractors.generators.TransactionMockGenerator.nextTransactionInYearMonth
import com.aradipatrik.claptrap.interactors.interfaces.todo.TransactionInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.joda.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class TransactionInteractorFake @Inject constructor() : TransactionInteractor {
  private val transactions = MutableStateFlow(mapOf(YearMonth.now() to initialTransactions))

  override fun getAllTransactionsFlow() = transactions
    .map { it.values.flatten() }
    .flowOn(Dispatchers.IO)

  override fun getAllTransactionsInYearMonthFlow(yearMonth: YearMonth) = transactions
    .map { it.getValue(yearMonth) }
    .also { populateTransactionsWithYearMonthIfMissing(yearMonth) }
    .flowOn(Dispatchers.IO)

  private fun populateTransactionsWithYearMonthIfMissing(yearMonth: YearMonth) {
    if (!transactions.value.containsKey(yearMonth)) {
      transactions.value = transactions.value +
        (yearMonth to (100 of { Random.nextTransactionInYearMonth(yearMonth) }))
    }
  }

  override suspend fun saveTransaction(transaction: Transaction) = withContext(Dispatchers.IO) {
    val yearMonth = YearMonth.fromDateFields(transaction.date.toDate())
    transactions.value = transactions.value +
      (yearMonth to ((transactions.value[yearMonth] ?: emptyList()) + transaction))
  }

  override suspend fun getTransaction(
    transactionId: String
  ): Transaction = withContext(Dispatchers.IO) {
    transactions.value.values.flatten().first { transactionId == it.id }
  }

  override suspend fun deleteTransaction(transactionId: String) {
    val oldTransaction = getTransaction(transactionId)
    val transactionYearMonth = YearMonth(oldTransaction.date.year, oldTransaction.date.monthOfYear)
    val oldTransactionsInYearMonth = transactions.value[transactionYearMonth]

    require(oldTransactionsInYearMonth != null) {
      "There should be transactions in year month: $transactionYearMonth"
    }

    transactions.value = transactions.value + (transactionYearMonth to
      oldTransactionsInYearMonth.filter { it.id != transactionId })
  }
}

internal val initialTransactions = 100 of { Random.nextTransactionInYearMonth(YearMonth.now()) }
