package com.aradipatrik.claptrap.feature.transactions.edit.ui

import androidx.fragment.app.viewModels
import com.aradipatrik.claptrap.feature.transactions.R
import com.aradipatrik.claptrap.feature.transactions.databinding.FragmentEditTransactionBinding
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewEffect
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewEvent
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewModel
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewState
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

@AndroidEntryPoint
class EditTransactionFragment :
  ClapTrapFragment<EditTransactionViewState, EditTransactionViewEvent, EditTransactionViewEffect, FragmentEditTransactionBinding>(
    R.layout.fragment_edit_transaction, FragmentEditTransactionBinding::inflate
  ) {
  private val transactionId by lazy {
    requireArguments().getString("transactionId") ?: error("transactionId is required")
  }

  @Inject
  lateinit var viewModelFactory: EditTransactionViewModel.AssistedFactory

  override val viewModel by viewModels<EditTransactionViewModel> {
    EditTransactionViewModel.provideFactory(viewModelFactory, transactionId)
  }

  override val viewEvents: Flow<EditTransactionViewEvent> get() = emptyFlow()

  override fun render(viewState: EditTransactionViewState) {
    binding.transactionIdDisplay.text = (viewState as EditTransactionViewState.Placeholder).transactionId
  }

  override fun react(viewEffect: EditTransactionViewEffect) {

  }
}