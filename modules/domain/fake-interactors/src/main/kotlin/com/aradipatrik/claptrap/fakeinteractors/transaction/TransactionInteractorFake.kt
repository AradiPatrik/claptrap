package com.aradipatrik.claptrap.fakeinteractors.transaction

import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.interactors.interfaces.todo.TransactionInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class TransactionInteractorFake @Inject constructor() : TransactionInteractor {
  private val transactions = MutableStateFlow(initialTransactions)

  override fun getAllTransactions() = transactions
}
