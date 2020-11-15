package com.aradipatrik.claptrap.fakeinteractors.todo

import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.interactors.interfaces.todo.TransactionInteractor
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class TransactionInteractorFake @Inject constructor() : TransactionInteractor {
  override fun getAllTransactions() = emptyFlow<List<Transaction>>()
}
