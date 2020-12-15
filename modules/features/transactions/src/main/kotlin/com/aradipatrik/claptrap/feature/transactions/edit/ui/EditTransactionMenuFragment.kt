package com.aradipatrik.claptrap.feature.transactions.edit.ui

import android.os.Bundle
import com.aradipatrik.claptrap.common.backdrop.FragmentExt.menuDestinationViewModels
import com.aradipatrik.claptrap.feature.transactions.R
import com.aradipatrik.claptrap.feature.transactions.databinding.FragmentMenuEditTransactionBinding
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewEffect
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewEvent
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewEvent.BackClick
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewModel
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewState
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import ru.ldralighieri.corbind.view.clicks

@AndroidEntryPoint
class EditTransactionMenuFragment : ClapTrapFragment<
  EditTransactionViewState,
  EditTransactionViewEvent,
  EditTransactionViewEffect,
  FragmentMenuEditTransactionBinding>(
  R.layout.fragment_menu_edit_transaction, FragmentMenuEditTransactionBinding::inflate
) {
  override val viewModel by menuDestinationViewModels<EditTransactionViewModel>()

  override val viewEvents
    get() = binding.backButton.clicks().map { BackClick }

  override fun initViews(savedInstanceState: Bundle?) {
    binding.backButton.morph()
    binding.backButton.shouldAnimateAutomaticallyOnClicks = false
  }

  override fun render(viewState: EditTransactionViewState) {
  }

  override fun react(viewEffect: EditTransactionViewEffect) {
  }
}