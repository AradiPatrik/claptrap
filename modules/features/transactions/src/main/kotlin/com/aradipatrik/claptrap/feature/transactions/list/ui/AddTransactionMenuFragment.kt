package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.transition.TransitionManager
import com.aradipatrik.claptrap.common.backdrop.BackEffect
import com.aradipatrik.claptrap.common.backdrop.BackListener
import com.aradipatrik.claptrap.feature.transactions.R
import com.aradipatrik.claptrap.feature.transactions.databinding.FragmentAddTransactionMenuBinding
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEffect
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewModel
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewState
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import ru.ldralighieri.corbind.view.clicks
import timber.log.Timber

class AddTransactionMenuFragment : ClapTrapFragment<
  TransactionsViewState,
  TransactionsViewEvent,
  TransactionsViewEffect,
  FragmentAddTransactionMenuBinding
  >(R.layout.fragment_add_transaction_menu, FragmentAddTransactionMenuBinding::inflate) {
  override val viewModel by activityViewModels<TransactionsViewModel>()
  override val viewEvents get() = binding.backButton.clicks()
    .map { TransactionsViewEvent.BackClick }

  override fun initViews(savedInstanceState: Bundle?) {
    binding.backButton.morph()
  }

  override fun render(viewState: TransactionsViewState) {
  }

  override fun react(viewEffect: TransactionsViewEffect) {
  }

  init {
    require(true)
  }
}
