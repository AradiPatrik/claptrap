package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.os.Bundle
import androidx.fragment.app.activityViewModels
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import timber.log.Timber

class AddTransactionMenuFragment : ClapTrapFragment<
  TransactionsViewState,
  TransactionsViewEvent,
  TransactionsViewEffect,
  FragmentAddTransactionMenuBinding
  >(R.layout.fragment_add_transaction_menu, FragmentAddTransactionMenuBinding::inflate), BackListener {
  override val viewModel by activityViewModels<TransactionsViewModel>()
  override val viewEvents get() = emptyFlow<TransactionsViewEvent>()

  override fun initViews(savedInstanceState: Bundle?) {
    if (savedInstanceState == null) {
      binding.backButton.morph()
    }
  }

  override fun render(viewState: TransactionsViewState) {
  }

  override fun react(viewEffect: TransactionsViewEffect) {
  }

  override fun onBack(): BackEffect {
    return BackEffect.NO_POP
  }

}
