package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.annotation.SuppressLint
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
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
import com.aradipatrik.claptrap.theme.widget.ViewUtil.getAnimatedVectorDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
    binding.fabBackground.clicks().map { ActionClick },
    binding.frontLayer.clicks().map { ActionClick },
    backPressEvents.consumeAsFlow().map { BackClick },
    binding.numberPad.digitClicks.map { NumberClick(it) },
    binding.numberPad.plusClicks.map { PlusClick },
    binding.numberPad.minusClicks.map { MinusClick },
    binding.numberPad.pointClicks.map { PointClick },
    binding.numberPad.deleteOneClicks.map { DeleteOneClick },
    binding.numberPad.actionClicks.map { NumberPadActionClick }
  )

  private val backPressEvents = Channel<Unit>(BUFFERED)

  private val transactionAdapter by lazy { TransactionAdapter() }
  private val categoryAdapter by lazy { CategoryAdapter() }

  private val checkToEquals by lazy { getAnimatedVectorDrawable(R.drawable.check_to_equals) }
  private val equalsToCheck by lazy { getAnimatedVectorDrawable(R.drawable.equals_to_check) }
  private val plusToCheck by lazy { getAnimatedVectorDrawable(R.drawable.plus_to_check) }
  private val checkToPlus by lazy { getAnimatedVectorDrawable(R.drawable.check_to_plus) }

  @SuppressLint("BinaryOperationInTimber")
  override fun initViews(savedInstanceState: Bundle?) {
    binding.transactionRecyclerView.layoutManager = LinearLayoutManager(context)
    binding.transactionRecyclerView.adapter = transactionAdapter

    binding.categoryRecyclerView.layoutManager = GridLayoutManager(
      requireContext(), 3
    )
    binding.categoryRecyclerView.adapter = categoryAdapter

    transactionAdapter.headerChangeEvents
      .onEach(binding.transactionsHeader::text::set)
      .launchInWhenResumed(lifecycleScope)

    if (savedInstanceState != null) {
      binding.transactionsMotionLayout.restoreState(savedInstanceState, MOTION_LAYOUT_STATE_KEY)
      if (savedInstanceState.getBoolean(IS_ON_CALCULATOR)) {
        binding.fabIcon.startToEndAnimatedVectorDrawable = checkToEquals
        binding.fabIcon.endToStartAnimatedVectorDrawable = equalsToCheck
        binding.fabIcon.reset()
        binding.fabBackground.isEnabled = false
        binding.fabBackground.isClickable = false
      }
      if (!savedInstanceState.getBoolean(FAB_ICON_STATE_KEY)) binding.fabIcon.morph()
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    binding.transactionsMotionLayout.saveState(outState, MOTION_LAYOUT_STATE_KEY)
    outState.putBoolean(FAB_ICON_STATE_KEY, binding.fabIcon.isAtStartState)
    outState.putBoolean(
      IS_ON_CALCULATOR, binding.fabIcon.startToEndAnimatedVectorDrawable == checkToEquals
    )
  }

  override fun render(viewState: TransactionsViewState) = when (viewState) {
    TransactionsViewState.Loading -> renderLoading()
    is TransactionsViewState.TransactionsLoaded -> renderLoaded(viewState)
    is TransactionsViewState.Adding -> renderAdding(viewState)
  }

  private fun renderAdding(viewState: TransactionsViewState.Adding) {
    binding.numberPad.calculatorDisplayText = viewState.calculatorState.asDisplayText
    categoryAdapter.submitList(viewState.categories)
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
    TransactionsViewEffect.HideTransactionMenu -> backdrop.clearMenu()
    TransactionsViewEffect.PlayAddAnimation -> playAddAnimation()
    TransactionsViewEffect.PlayReverseAddAnimation -> playReverseAddAnimation()
    TransactionsViewEffect.Back -> backdrop.back()
    TransactionsViewEffect.MorphCheckToEquals -> binding.fabIcon.morph()
    TransactionsViewEffect.MorphEqualsToCheck -> binding.fabIcon.morph()
  }

  private fun playReverseAddAnimation() = lifecycleScope.launchWhenResumed {
    with(binding.transactionsMotionLayout) {
      if (!binding.fabIcon.isAtStartState) {
        binding.fabIcon.morph()
      }
      binding.fabIcon.isAtStartState = false
      binding.fabIcon.startToEndAnimatedVectorDrawable = plusToCheck
      binding.fabIcon.endToStartAnimatedVectorDrawable = checkToPlus
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
      binding.fabIcon.isAtStartState = true
      binding.fabIcon.startToEndAnimatedVectorDrawable = checkToEquals
      binding.fabIcon.endToStartAnimatedVectorDrawable = equalsToCheck
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
    private const val IS_ON_CALCULATOR = "IS_ON_CALCULATOR"
  }
}
