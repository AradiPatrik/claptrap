package com.aradipatrik.claptrap.feature.transactions.edit.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewEffect.Back
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewState.Editing
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewState.Loading
import com.aradipatrik.claptrap.interactors.interfaces.todo.TransactionInteractor
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel
import com.aradipatrik.claptrap.mvi.MviUtil.ignore
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch
import timber.log.Timber

class EditTransactionViewModel @AssistedInject constructor(
  private val transactionInteractor: TransactionInteractor,
  @Assisted private val transactionId: String
) : ClaptrapViewModel<EditTransactionViewState,
  EditTransactionViewEvent,
  EditTransactionViewEffect>(Loading) {
  init {
    reduceState {
      Editing(transactionInteractor.getTransaction(transactionId))
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
    EditTransactionViewEvent.BackClick -> goBack()
  }

  private fun goBack() = viewModelScope.launch { viewEffects.emit(Back) }
    .ignore()

  init {
    Timber.tag("APDEBUG").d("initied edited transaction view model")
  }
}
