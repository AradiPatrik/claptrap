package com.aradipatrik.claptrap.di

import com.aradipatrik.claptrap.fakeinteractors.category.CategoryInteractorFake
import com.aradipatrik.claptrap.fakeinteractors.todo.TodoInteractorFake
import com.aradipatrik.claptrap.fakeinteractors.transaction.TransactionInteractorFake
import com.aradipatrik.claptrap.fakeinteractors.wallet.WalletInteractorFake
import com.aradipatrik.claptrap.interactors.interfaces.todo.CategoryInteractor
import com.aradipatrik.claptrap.interactors.interfaces.todo.TodoInteractor
import com.aradipatrik.claptrap.interactors.interfaces.todo.TransactionInteractor
import com.aradipatrik.claptrap.interactors.interfaces.todo.WalletInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class InteractorModule {
  @Binds
  abstract fun bindTodoInteractor(interactor: TodoInteractorFake): TodoInteractor

  @Binds
  abstract fun bindTransactionInteractor(
    interactor: TransactionInteractorFake
  ): TransactionInteractor

  @Binds
  abstract fun bindCategoryInteractor(
    interactor: CategoryInteractorFake
  ): CategoryInteractor

  @Binds
  abstract fun bindWalletInteractor(
    interactor: WalletInteractorFake
  ) : WalletInteractor
}
