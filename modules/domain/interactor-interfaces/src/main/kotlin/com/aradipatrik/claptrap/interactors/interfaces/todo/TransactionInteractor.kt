package com.aradipatrik.claptrap.interactors.interfaces.todo

import com.aradipatrik.claptrap.domain.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionInteractor {
  fun getAllTransactions(): Flow<List<Transaction>>
}
