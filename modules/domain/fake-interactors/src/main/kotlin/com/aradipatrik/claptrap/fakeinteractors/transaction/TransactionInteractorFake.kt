package com.aradipatrik.claptrap.fakeinteractors.transaction

import com.aradipatrik.claptrap.fakeinteractors.generators.CommonMockGenerator.of
import com.aradipatrik.claptrap.fakeinteractors.generators.TransactionMockGenerator.nextTransaction
import com.aradipatrik.claptrap.interactors.interfaces.todo.TransactionInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.random.Random

class TransactionInteractorFake @Inject constructor() : TransactionInteractor {
  private val transactions = MutableStateFlow(initialTransactions)

  override fun getAllTransactions() = transactions
}

internal val initialTransactions = 50 of { Random.nextTransaction() }
