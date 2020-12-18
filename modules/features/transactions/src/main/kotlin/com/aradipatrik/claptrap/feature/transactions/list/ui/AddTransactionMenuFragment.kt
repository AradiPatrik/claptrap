package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.aradipatrik.claptrap.feature.transactions.R
import com.aradipatrik.claptrap.feature.transactions.databinding.FragmentMenuAddTransctionBinding
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEffect
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent.BackClick
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewModel
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewState
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.theme.widget.AnimationConstants.QUICK_ANIMATION_DURATION
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.ldralighieri.corbind.view.clicks

class AddTransactionMenuFragment : ClapTrapFragment<
  TransactionsViewState,
  TransactionsViewEvent,
  TransactionsViewEffect,
  FragmentMenuAddTransctionBinding
  >(R.layout.fragment_menu_add_transction, FragmentMenuAddTransctionBinding::inflate) {
  override val viewModel by activityViewModels<TransactionsViewModel>()

  override val viewEvents: Flow<TransactionsViewEvent> get() = binding.backButton.clicks()
    .map { BackClick }

  override fun initViews(savedInstanceState: Bundle?) {
    binding.backButton.morph()
    fadeInExpenseIncomeChips()
  }

  private fun fadeInExpenseIncomeChips() = viewLifecycleOwner.lifecycleScope.launchWhenResumed {
    delay(QUICK_ANIMATION_DURATION)
    binding.expenseChip.animate()
      .setDuration(QUICK_ANIMATION_DURATION)
      .alpha(1.0f)
      .start()

    binding.incomeChip.animate()
      .setDuration(QUICK_ANIMATION_DURATION)
      .alpha(1.0f)
      .start()
  }

  override fun render(viewState: TransactionsViewState) {
  }

  override fun react(viewEffect: TransactionsViewEffect) {
  }


}
