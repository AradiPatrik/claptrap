package com.aradipatrik.claptrap.feature.transactions.edit.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewEffect.Back
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewState.Editing
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewState.Loading
import com.aradipatrik.claptrap.interactors.interfaces.todo.CategoryInteractor
import com.aradipatrik.claptrap.interactors.interfaces.todo.TransactionInteractor
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import org.joda.money.CurrencyUnit
import org.joda.money.Money

class EditTransactionViewModel @AssistedInject constructor(
  private val transactionInteractor: TransactionInteractor,
  private val categoryInteractor: CategoryInteractor,
  @Assisted private val transactionId: String
) : ClaptrapViewModel<EditTransactionViewState,
  EditTransactionViewEvent,
  EditTransactionViewEffect>(Loading) {
  init {
    reduceState {
      val transaction = transactionInteractor.getTransaction(transactionId)
      val categories = categoryInteractor.getAllCategories().take(1).single()
      Editing(
        memo = transaction.memo,
        amount = transaction.money.amount.toPlainString(),
        date = transaction.date,
        category = transaction.category,
        isCategorySelectorShowing = false,
        categories = categories
      )
    }
  }

  @AssistedInject.Factory
  interface AssistedFactory {
    fun create(transactionId: String): EditTransactionViewModel
  }

  companion object {
    @Suppress("UNCHECKED_CAST")
    fun provideFactory(
      assistedFactory: AssistedFactory,
      transactionId: String
    ) = object : ViewModelProvider.Factory {
      override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return assistedFactory.create(transactionId) as T
      }
    }
  }

  override fun processInput(viewEvent: EditTransactionViewEvent) = when (viewEvent) {
    EditTransactionViewEvent.BackClick -> closeCategorySelectorOrGoBack()
    EditTransactionViewEvent.DeleteButtonClick -> deleteAndNavigateBack()
    is EditTransactionViewEvent.MemoChange -> changeMemoTo(viewEvent.memo)
    is EditTransactionViewEvent.AmountChange -> changeAmountTo(viewEvent.amount)
    EditTransactionViewEvent.EditDoneClick -> saveCurrentTransactionAndNavigateBack()
    EditTransactionViewEvent.CategorySelectorClick -> openCategorySelector()
    EditTransactionViewEvent.ScrimClick -> closeCategorySelector()
  }

  private fun closeCategorySelector() = reduceSpecificState<Editing> { state ->
    state.copy(isCategorySelectorShowing = false)
  }

  private fun openCategorySelector() = reduceSpecificState<Editing> { state ->
    state.copy(isCategorySelectorShowing = true)
  }

  private fun saveCurrentTransactionAndNavigateBack() = withState<Editing> { state ->
    transactionInteractor.saveTransaction(
      Transaction(
        id = transactionId,
        money = Money.of(CurrencyUnit.USD, state.amount.toDouble()),
        date = state.date,
        category = state.category,
        memo = state.memo
      )
    )
    viewEffects.emit(Back)
  }

  private fun changeAmountTo(newAmount: String) = reduceSpecificState<Editing> { state ->
    state.copy(amount = newAmount)
  }

  private fun changeMemoTo(newMemo: String) = reduceSpecificState<Editing> { state ->
    state.copy(memo = newMemo)
  }

  private fun deleteAndNavigateBack() = sideEffect {
    transactionInteractor.deleteTransaction(transactionId)
    viewEffects.emit(Back)
  }

  private fun closeCategorySelectorOrGoBack() = reduceState { state ->
    when (state) {
      is Loading -> state.also { viewEffects.emit(Back) }
      is Editing -> if (state.isCategorySelectorShowing) {
        state.copy(isCategorySelectorShowing = false)
      } else {
        state.also { viewEffects.emit(Back) }
      }
    }
  }
}
