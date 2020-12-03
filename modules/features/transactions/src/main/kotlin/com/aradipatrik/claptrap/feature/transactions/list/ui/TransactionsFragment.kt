package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent.ActionClick
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent.AddTransactionViewEvent.CalculatorEvent.*
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent.BackClick
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
    binding.fabBackground.clicks().map { ActionClick },
    binding.frontLayer.clicks().map { ActionClick },
    backPressEvents.consumeAsFlow().map { BackClick },
    binding.numberPad.digitClicks.map { NumberClick(it) },
    binding.numberPad.plusClicks.map { PlusClick },
    binding.numberPad.minusClicks.map { MinusClick },
    binding.numberPad.pointClicks.map { PointClick },
    binding.numberPad.deleteOneClicks.map { DeleteOneClick }
  )

  private val backPressEvents = Channel<Unit>(BUFFERED)

  private val transactionAdapter by lazy { TransactionAdapter() }

  @SuppressLint("BinaryOperationInTimber")
  override fun initViews(savedInstanceState: Bundle?) {
    binding.transactionRecyclerView.layoutManager = LinearLayoutManager(context)
    binding.transactionRecyclerView.adapter = transactionAdapter

    transactionAdapter.headerChangeEvents
      .onEach(binding.transactionsHeader::text::set)
      .launchInWhenResumed(lifecycleScope)

    if (savedInstanceState != null) {
      binding.transactionsMotionLayout.restoreState(savedInstanceState, MOTION_LAYOUT_STATE_KEY)
      if (!savedInstanceState.getBoolean(FAB_ICON_STATE_KEY)) binding.fabIcon.morph()
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    binding.transactionsMotionLayout.saveState(outState, MOTION_LAYOUT_STATE_KEY)
    outState.putBoolean(FAB_ICON_STATE_KEY, binding.fabIcon.isAtStartState)
  }

  override fun render(viewState: TransactionsViewState) = when (viewState) {
    TransactionsViewState.Loading -> renderLoading()
    is TransactionsViewState.TransactionsLoaded -> renderLoaded(viewState)
    is TransactionsViewState.Adding -> renderAdding(viewState)
  }

  private fun renderAdding(viewState: TransactionsViewState.Adding) {
    binding.numberPad.calculatorDisplayText = viewState.calculatorState.asDisplayText
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
    TransactionsViewEffect.HiedTransactionMenu -> backdrop.clearMenu()
    TransactionsViewEffect.PlayAddAnimation -> playAddAnimation()
    TransactionsViewEffect.PlayReverseAddAnimation -> playReverseAddAnimation()
    TransactionsViewEffect.Back -> backdrop.back()
  }

  private fun playReverseAddAnimation() = lifecycleScope.launchWhenResumed {
    with(binding.transactionsMotionLayout) {
      playReverseTransitionAndWaitForFinish(R.id.action_visible, R.id.categories_visible)
      playReverseTransitionAndWaitForFinish(R.id.fab_at_middle, R.id.action_visible)
      playReverseTransitionAndWaitForFinish(R.id.fab_at_bottom, R.id.fab_at_middle)
      binding.fabIcon.morph()
      binding.fabBackground.isClickable = true
    }
  }.ignore()

  private fun playAddAnimation() = lifecycleScope.launchWhenResumed {
    with(binding.transactionsMotionLayout) {
      binding.fabBackground.isClickable = false
      playTransitionAndWaitForFinish(R.id.fab_at_bottom, R.id.fab_at_middle)
      playTransitionAndWaitForFinish(R.id.fab_at_middle, R.id.action_visible)
      playTransitionAndWaitForFinish(R.id.action_visible, R.id.categories_visible)
      binding.fabIcon.morph()
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
    private const val FAB_ICON_STATE_KEY = "FAB_ICON_STATE_KEY"
  }
}
