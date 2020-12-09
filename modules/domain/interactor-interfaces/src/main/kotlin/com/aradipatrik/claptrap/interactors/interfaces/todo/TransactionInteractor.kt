package com.aradipatrik.claptrap.interactors.interfaces.todo

import com.aradipatrik.claptrap.domain.Transaction
import kotlinx.coroutines.flow.Flow
import org.joda.time.YearMonth

interface TransactionInteractor {
  fun getAllTransactions(): Flow<List<Transaction>>

  fun getAllTransactionsInYearMonth(yearMonth: YearMonth): Flow<List<Transaction>>
}
