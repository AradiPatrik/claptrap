package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aradipatrik.claptrap.common.backdrop.BackEffect
import com.aradipatrik.claptrap.common.backdrop.BackListener
import com.aradipatrik.claptrap.common.backdrop.backdrop
import com.aradipatrik.claptrap.feature.transactions.R
import com.aradipatrik.claptrap.feature.transactions.databinding.FragmentTransactionsBinding
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEffect
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewModel
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewState
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.mvi.Flows.launchInWhenResumed
import com.aradipatrik.claptrap.mvi.MviUtil.ignore
import com.aradipatrik.claptrap.theme.widget.MotionUtil.playReverseTransitionAndWaitForFinish
import com.aradipatrik.claptrap.theme.widget.MotionUtil.playTransitionAndWaitForFinish
import com.aradipatrik.claptrap.theme.widget.MotionUtil.restoreState
import com.aradipatrik.claptrap.theme.widget.MotionUtil.saveState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.*
import ru.ldralighieri.corbind.view.clicks
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TransactionsFragment : ClapTrapFragment<
  TransactionsViewState,
  TransactionsViewEvent,
  TransactionsViewEffect,
  FragmentTransactionsBinding
  >(R.layout.fragment_transactions, FragmentTransactionsBinding::inflate), BackListener {

  @Inject
  lateinit var transactionListBuilderDelegate: TransactionListBuilderDelegate

  override val viewModel by activityViewModels<TransactionsViewModel>()

  override val viewEvents: Flow<TransactionsViewEvent> get() = merge(
    binding.fabBackground.clicks()
      .map { TransactionsViewEvent.AddClick },
    binding.frontLayer.clicks()
      .map { TransactionsViewEvent.AddClick },
    backPressEvents.consumeAsFlow()
      .map { TransactionsViewEvent.BackClick }
  )

  private val backPressEvents = Channel<Unit>(BUFFERED)

  private val transactionAdapter by lazy { TransactionAdapter() }

  @SuppressLint("BinaryOperationInTimber")
  override fun initViews(savedInstanceState: Bundle?) {
    binding.transactionRecyclerView.layoutManager = LinearLayoutManager(context)
    binding.transactionRecyclerView.adapter = transactionAdapter

    transactionAdapter.headerChangeEvents
      .onEach(binding.transactionsHeader::setText)
      .launchInWhenResumed(lifecycleScope)

    if (savedInstanceState != null) {
      binding.transactionsMotionLayout.restoreState(savedInstanceState, MOTION_LAYOUT_STATE_KEY)
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    binding.transactionsMotionLayout.saveState(outState, MOTION_LAYOUT_STATE_KEY)
  }

  override fun render(viewState: TransactionsViewState) = when (viewState) {
    TransactionsViewState.Loading -> renderLoading()
    is TransactionsViewState.TransactionsLoaded -> renderLoaded(viewState)
    TransactionsViewState.Adding -> renderAdding(viewState)
  }

  private fun renderAdding(viewState: TransactionsViewState) {
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
    TransactionsViewEffect.HiedTransactionMenu -> backdrop.clearMenu().also {
      Timber.d("Clear menu called")
    }
    TransactionsViewEffect.PlayAddAnimation -> playAddAnimation()
    TransactionsViewEffect.PlayReverseAddAnimation -> playReverseAddAnimation()
  }.also {
    Timber.d("viewEffect: ${viewEffect}")
  }

  private fun playReverseAddAnimation() = lifecycleScope.launchWhenResumed {
    with(binding.transactionsMotionLayout) {
      playReverseTransitionAndWaitForFinish(R.id.action_visible, R.id.categories_visible)
      playReverseTransitionAndWaitForFinish(R.id.fab_at_middle, R.id.action_visible)
      playReverseTransitionAndWaitForFinish(R.id.fab_at_bottom, R.id.fab_at_middle)
    }
  }.ignore()

  private fun playAddAnimation() = lifecycleScope.launchWhenResumed {
    with(binding.transactionsMotionLayout) {
      playTransitionAndWaitForFinish(R.id.fab_at_bottom, R.id.fab_at_middle)
      playTransitionAndWaitForFinish(R.id.fab_at_middle, R.id.action_visible)
      playTransitionAndWaitForFinish(R.id.action_visible, R.id.categories_visible)
    }
  }.ignore()

  override fun onBack(): BackEffect {
    lifecycleScope.launchWhenResumed {
      backPressEvents.send(Unit)
    }
    return BackEffect.NO_POP
  }

  companion object {
    private const val MOTION_LAYOUT_STATE_KEY = "TRANSACTION_MOTION_LAYOUT_STATE"
  }
}
