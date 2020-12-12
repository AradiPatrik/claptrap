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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class EditTransactionFragment :
  ClapTrapFragment<EditTransactionViewState, EditTransactionViewEvent, EditTransactionViewEffect, FragmentEditTransactionBinding>(
    R.layout.fragment_edit_transaction, FragmentEditTransactionBinding::inflate
  ) {
  override val viewModel by viewModels<EditTransactionViewModel>()

  override val viewEvents: Flow<EditTransactionViewEvent> get() = emptyFlow()

  override fun render(viewState: EditTransactionViewState) {

  }

  override fun react(viewEffect: EditTransactionViewEffect) {

  }
}