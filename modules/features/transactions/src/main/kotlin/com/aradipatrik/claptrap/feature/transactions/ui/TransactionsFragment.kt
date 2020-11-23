package com.aradipatrik.claptrap.feature.transactions.ui

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.feature.transactions.R
import com.aradipatrik.claptrap.feature.transactions.databinding.FragmentTransactionsBinding
import com.aradipatrik.claptrap.feature.transactions.model.*
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.mvi.Flows.launchInWhenResumed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class TransactionsFragment : ClapTrapFragment<
  TransactionsViewState,
  TransactionsViewEvent,
  TransactionsViewEffect,
  FragmentTransactionsBinding
  >(R.layout.fragment_transactions, FragmentTransactionsBinding::inflate) {

  @Inject
  lateinit var transactionListBuilderDelegate: TransactionListBuilderDelegate

  override val viewModel by viewModels<TransactionsViewModel>()
  override val viewEvents: Flow<TransactionsViewEvent> = emptyFlow()

  private val transactionAdapter by lazy { TransactionAdapter() }

  override fun initViews() {
    binding.transactionRecyclerView.layoutManager = LinearLayoutManager(context)
    binding.transactionRecyclerView.adapter = transactionAdapter

    transactionAdapter.headerChangeEvents
      .onEach(binding.transactionsHeader::setText)
      .launchInWhenResumed(lifecycleScope)
  }

  override fun render(viewState: TransactionsViewState) = when (viewState) {
    TransactionsViewState.Loading -> renderLoading()
    is TransactionsViewState.Loaded -> renderLoaded(viewState)
  }

  private fun renderLoading() {
  }

  private fun renderLoaded(viewState: TransactionsViewState.Loaded) {
    transactionAdapter.submitList(
      transactionListBuilderDelegate.generateListItemsFrom(viewState.transactions)
    )
  }

  override fun react(viewEffect: TransactionsViewEffect) {

  }

}
