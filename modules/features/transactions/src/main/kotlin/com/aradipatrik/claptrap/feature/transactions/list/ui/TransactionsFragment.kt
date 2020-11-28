package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aradipatrik.claptrap.common.backdrop.backdrop
import com.aradipatrik.claptrap.feature.transactions.R
import com.aradipatrik.claptrap.feature.transactions.databinding.FragmentTransactionsBinding
import com.aradipatrik.claptrap.feature.transactions.list.model.*
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.mvi.Flows.launchInWhenResumed
import com.aradipatrik.claptrap.mvi.MviUtil.ignore
import com.aradipatrik.claptrap.theme.widget.MotionUtil.playTransitionAndWaitForFinish
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import ru.ldralighieri.corbind.view.clicks
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

  override val viewModel by activityViewModels<TransactionsViewModel>()

  override val viewEvents: Flow<TransactionsViewEvent> get() = merge(
    binding.fabBackground.clicks()
      .map { TransactionsViewEvent.AddClick },
    binding.frontLayer.clicks()
      .map { TransactionsViewEvent.AddClick }
  )

  private val transactionAdapter by lazy { TransactionAdapter() }

  override fun initViews(savedInstanceState: Bundle?) {
    binding.transactionRecyclerView.layoutManager = LinearLayoutManager(context)
    binding.transactionRecyclerView.adapter = transactionAdapter

    transactionAdapter.headerChangeEvents
      .onEach(binding.transactionsHeader::setText)
      .launchInWhenResumed(lifecycleScope)
  }

  override fun render(viewState: TransactionsViewState) = when (viewState) {
    TransactionsViewState.Loading -> renderLoading()
    is TransactionsViewState.TransactionsLoaded -> renderLoaded(viewState)
    TransactionsViewState.Adding -> renderAdding(viewState)
  }

  private fun renderAdding(viewState: TransactionsViewState) = lifecycleScope.launchWhenResumed {
    playAddAnimation()
  }.ignore()

  private suspend fun playAddAnimation() = with(binding.transactionsMotionLayout) {
    playTransitionAndWaitForFinish(R.id.fab_at_bottom, R.id.fab_at_middle)
    playTransitionAndWaitForFinish(R.id.fab_at_middle, R.id.action_visible)
    playTransitionAndWaitForFinish(R.id.action_visible, R.id.categories_visible)
  }

  private fun renderLoading() {
  }

  private fun renderLoaded(viewState: TransactionsViewState.TransactionsLoaded) {
    transactionAdapter.submitList(
      transactionListBuilderDelegate.generateListItemsFrom(viewState.transactions)
    )
  }

  override fun react(viewEffect: TransactionsViewEffect) = when(viewEffect) {
    is TransactionsViewEffect.ShowAddTransactionMenu -> backdrop
      .switchMenu(AddTransactionMenuFragment())
    TransactionsViewEffect.HiedTransactionMenu -> backdrop
      .clearMenu()
  }

  companion object {
    private const val MOTION_LAYOUT_STATE_KEY = "MOTION_LAYOUT_STATE_KEY2"
  }
}
