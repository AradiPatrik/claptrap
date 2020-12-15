package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aradipatrik.claptrap.common.backdrop.BackEffect
import com.aradipatrik.claptrap.common.backdrop.BackListener
import com.aradipatrik.claptrap.common.backdrop.backdrop
import com.aradipatrik.claptrap.feature.transactions.R
import com.aradipatrik.claptrap.feature.transactions.common.CategoryListItem
import com.aradipatrik.claptrap.feature.transactions.databinding.FragmentTransactionsBinding
import com.aradipatrik.claptrap.feature.transactions.databinding.ListItemTransactionItemBinding
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEffect
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent.*
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent.AddTransactionViewEvent.*
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent.AddTransactionViewEvent.CalculatorEvent.*
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewModel
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewState
import com.aradipatrik.claptrap.feature.transactions.mapper.CategoryIconMapper.drawableRes
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.mvi.Flows.launchInWhenResumed
import com.aradipatrik.claptrap.mvi.MviUtil.ignore
import com.aradipatrik.claptrap.theme.widget.MotionUtil.awaitEnd
import com.aradipatrik.claptrap.theme.widget.MotionUtil.playReverseTransitionAndWaitForFinish
import com.aradipatrik.claptrap.theme.widget.MotionUtil.playTransitionAndWaitForFinish
import com.aradipatrik.claptrap.theme.widget.MotionUtil.restoreState
import com.aradipatrik.claptrap.theme.widget.MotionUtil.saveState
import com.aradipatrik.claptrap.theme.widget.ViewThemeUtil.getAnimatedVectorDrawable
import com.aradipatrik.claptrap.theme.widget.ViewThemeUtil.showAndWaitWith
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import org.joda.time.DateTime
import ru.ldralighieri.corbind.view.clicks
import javax.inject.Inject
import kotlin.math.max

@AndroidEntryPoint
class TransactionsFragment : ClapTrapFragment<
  TransactionsViewState,
  TransactionsViewEvent,
  TransactionsViewEffect,
  FragmentTransactionsBinding
  >(R.layout.fragment_transactions, FragmentTransactionsBinding::inflate), BackListener {

  @Inject lateinit var transactionListBuilderDelegate: TransactionListBuilderDelegate
  @Inject lateinit var transactionAdapterFactory: TransactionAdapter.Factory

  override val viewModel by activityViewModels<TransactionsViewModel>()

  override val viewEvents: Flow<TransactionsViewEvent>
    get() = merge(
      binding.fabBackground.clicks().map { ActionClick },
      binding.numberPad.digitClicks.map { NumberClick(it) },
      binding.numberPad.plusClicks.map { PlusClick },
      binding.numberPad.minusClicks.map { MinusClick },
      binding.numberPad.pointClicks.map { PointClick },
      binding.numberPad.deleteOneClicks.map { DeleteOneClick },
      binding.numberPad.actionClicks.map { NumberPadActionClick },
      categoryAdapter.categorySelectedEvents.map { CategorySelected(it.category) },
      binding.numberPad.memoChanges.map { MemoChange(it) },
      binding.numberPad.calendarClicks.map { CalendarClick },
      binding.yearSelectorButton.clicks().map { YearMonthSelectorClick },
      binding.monthSelectionChipGroup.monthClicks.map { MonthSelected(it) },
      binding.yearDecreaseChevron.clicks().map { YearDecreased },
      binding.yearIncreaseChevron.clicks().map { YearIncreased },
      transactionAdapter.viewEvents
    )

  private val transactionAdapter by lazy { transactionAdapterFactory.create(lifecycleScope) }
  private val categoryAdapter by lazy { CategoryAdapter() }

  private val checkToEquals by lazy { getAnimatedVectorDrawable(R.drawable.check_to_equals) }
  private val equalsToCheck by lazy { getAnimatedVectorDrawable(R.drawable.equals_to_check) }
  private val plusToCheck by lazy { getAnimatedVectorDrawable(R.drawable.plus_to_check) }
  private val checkToPlus by lazy { getAnimatedVectorDrawable(R.drawable.check_to_plus) }
  private var isAnimationPlaying = false

  @SuppressLint("BinaryOperationInTimber")
  override fun initViews(savedInstanceState: Bundle?) {
    postponeEnterTransition()
    binding.transactionRecyclerView.viewTreeObserver.addOnPreDrawListener {
      startPostponedEnterTransition()
      true
    }
    binding.transactionRecyclerView.layoutManager = LinearLayoutManager(context)
    binding.transactionRecyclerView.adapter = transactionAdapter

    binding.categoryRecyclerView.layoutManager = GridLayoutManager(
      requireContext(), 3
    )
    binding.categoryRecyclerView.adapter = categoryAdapter

    transactionAdapter.headerChangeEvents
      .onEach(binding.transactionsHeader::text::set)
      .launchInWhenResumed(lifecycleScope)

    if (savedInstanceState != null && savedInstanceState.containsViewState()) {
      binding.transactionsMotionLayout.restoreState(savedInstanceState, MOTION_LAYOUT_STATE_KEY)

      if (savedInstanceState.getBoolean(IS_ON_CALCULATOR)) {
        binding.fabIcon.startToEndAnimatedVectorDrawable = checkToEquals
        binding.fabIcon.endToStartAnimatedVectorDrawable = equalsToCheck
        binding.fabIcon.reset()
        binding.fabBackground.isEnabled = false
        binding.fabBackground.isClickable = false
      }

      binding.yearSelectorButton.isActivated =
        savedInstanceState.getBoolean(IS_YEAR_MONTH_SELECTOR_ACTIVE_KEY)

      if (!savedInstanceState.getBoolean(FAB_ICON_STATE_KEY)) binding.fabIcon.morph()
    }
  }

  override fun saveViewState(outState: Bundle) {
    binding.transactionsMotionLayout.saveState(outState, MOTION_LAYOUT_STATE_KEY)
    outState.putBoolean(FAB_ICON_STATE_KEY, binding.fabIcon.isAtStartState)
    outState.putBoolean(
      IS_ON_CALCULATOR, binding.fabIcon.startToEndAnimatedVectorDrawable == checkToEquals
    )
    outState.putBoolean(IS_YEAR_MONTH_SELECTOR_ACTIVE_KEY, binding.yearSelectorButton.isActivated)
  }

  override fun render(viewState: TransactionsViewState) = when (viewState) {
    is TransactionsViewState.Loading -> renderLoading()
    is TransactionsViewState.TransactionsLoaded -> renderLoaded(viewState)
    is TransactionsViewState.Adding -> renderAdding(viewState)
  }

  private fun renderAdding(viewState: TransactionsViewState.Adding) {
    binding.numberPad.calculatorDisplayText = viewState.calculatorState.asDisplayText
    binding.numberPad.memo = viewState.memo
    binding.numberPad.date = viewState.date

    categoryAdapter.submitList(viewState.categories.map {
      CategoryListItem(it, it.id == viewState.selectedCategory?.id)
    })

    viewState.selectedCategory?.let { category ->
      binding.numberPad.setCategoryIconRes(category.icon.drawableRes)
    }

    if (!isAnimationPlaying && isUiInTransactionsState() || isYearMonthSelectorOpen()) {
      playAddAnimation()
    }
  }

  private fun renderLoading() {
  }

  private fun renderLoaded(viewState: TransactionsViewState.TransactionsLoaded) {
    transactionAdapter.submitList(
      transactionListBuilderDelegate.generateListItemsFrom(viewState.transactions)
    )

    binding.yearSelectionDisplay.text = viewState.yearMonth.year.toString()
    binding.monthSelectionChipGroup.selectedMonth = viewState.yearMonth.monthOfYear

    if (!isAnimationPlaying) {
      when {
        viewState.isYearMonthSelectorOpen && isYearMonthSelectorClosed() ->
          playShowYearMonthSelectorAnimation()
        !viewState.isYearMonthSelectorOpen && isYearMonthSelectorOpen() ->
          playHideYearMonthSelectorAnimation()
        isUiInAddingState() -> playReverseAddAnimation()
      }
    }
  }

  private fun isYearMonthSelectorOpen() =
    ViewCompat.isLaidOut(binding.transactionsMotionLayout) &&
      binding.transactionsMotionLayout.currentState == R.id.month_selector_shown

  private fun isYearMonthSelectorClosed() =
    ViewCompat.isLaidOut(binding.transactionsMotionLayout) &&
      binding.transactionsMotionLayout.currentState == R.id.fab_at_bottom

  private fun isUiInAddingState() =
    ViewCompat.isLaidOut(binding.transactionsMotionLayout) &&
      binding.transactionsMotionLayout.currentState == R.id.categories_visible

  private fun isUiInTransactionsState() =
    ViewCompat.isLaidOut(binding.transactionsMotionLayout) &&
      binding.transactionsMotionLayout.currentState == R.id.fab_at_bottom

  override fun react(viewEffect: TransactionsViewEffect) = when (viewEffect) {
    is TransactionsViewEffect.Back -> backdrop.back()
    is TransactionsViewEffect.ToggleNumberPadAction -> binding.fabIcon.morph()
    is TransactionsViewEffect.ShowDatePickerAt -> showDatePicker()
    is TransactionsViewEffect.ScrollToTransaction -> scrollTo(viewEffect.transactionId)
    is TransactionsViewEffect.NavigateToEditTransaction ->
      navigateToEdit(viewEffect.itemView, viewEffect.transactionId)
  }

  private fun navigateToEdit(
    clickedView: ListItemTransactionItemBinding,
    transactionId: String
  ) = lifecycleScope.launchWhenResumed {
    val arguments = bundleOf("transactionId" to transactionId)
    val extras = FragmentNavigatorExtras(
      clickedView.root to clickedView.root.transitionName,
      clickedView.transactionAmountIcon to clickedView.transactionAmountIcon.transitionName,
      clickedView.transactionDate to clickedView.transactionDate.transitionName,
      clickedView.transactionAmount to clickedView.transactionAmount.transitionName,
      clickedView.categoryIcon to clickedView.categoryIcon.transitionName,
      clickedView.transactionNote to clickedView.transactionNote.transitionName
    )

    val animator = ValueAnimator.ofFloat(0.0f, 1.0f)
      .apply {
        duration = 300
        addUpdateListener {
          val animatedValue = it.animatedValue as Float
          clickedView.root.cardElevation = (it.animatedValue as Float) * 32.0f
          binding.fabIcon.alpha = max(0.0f, 1.0f - animatedValue * 2)
          binding.fabIcon.scaleX = 1.0f - animatedValue
          binding.fabIcon.scaleY = 1.0f - animatedValue
          binding.fabBackground.scaleX = 1.0f - animatedValue
          binding.fabBackground.scaleY = 1.0f - animatedValue
        }
        interpolator = FastOutSlowInInterpolator()
      }

    animator.start()
    animator.awaitEnd()

    backdrop.backdropNavController
      .navigate(
        R.id.action_fragment_transactions_to_fragment_edit_transaction,
        arguments,
        null,
        extras
      )
  }.ignore()

  private fun scrollTo(transactionId: String) {
    transactionAdapter.currentScrollTargetId = transactionId
  }

  private suspend fun playAnimationWithMotionLayout(
    animationBlock: suspend MotionLayout.() -> Unit
  ) = with(binding.transactionsMotionLayout) {
    isAnimationPlaying = true
    animationBlock()
    isAnimationPlaying = false
  }

  private fun playHideYearMonthSelectorAnimation() = lifecycleScope.launchWhenResumed {
    playAnimationWithMotionLayout {
      binding.yearSelectorButton.isActivated = false
      playReverseTransitionAndWaitForFinish(R.id.fab_at_bottom, R.id.month_selector_shown)
    }
  }.ignore()

  private fun playShowYearMonthSelectorAnimation() = lifecycleScope.launchWhenResumed {
    playAnimationWithMotionLayout {
      binding.yearSelectorButton.isActivated = true
      playTransitionAndWaitForFinish(R.id.fab_at_bottom, R.id.month_selector_shown)
    }
  }.ignore()

  private fun showDatePicker() = lifecycleScope.launchWhenResumed {
    val selectedDateInstant = MaterialDatePicker.Builder
      .datePicker()
      .build()
      .showAndWaitWith(childFragmentManager)

    extraViewEventsFlow.emit(DateSelected(DateTime(selectedDateInstant)))
  }.ignore()

  private fun playReverseAddAnimation() = lifecycleScope.launchWhenResumed {
    playAnimationWithMotionLayout {
      backdrop.clearMenu()
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
      binding.fabBackground.isEnabled = true
      binding.fabBackground.isClickable = true
    }
  }.ignore()

  private fun playAddAnimation() = lifecycleScope.launchWhenResumed {
    playAnimationWithMotionLayout {
      if (isYearMonthSelectorOpen()) {
        binding.yearSelectorButton.isActivated = false
        playReverseTransitionAndWaitForFinish(R.id.fab_at_bottom, R.id.month_selector_shown)
      }

      backdrop.switchMenu(AddTransactionMenuFragment::class.java)
      binding.fabBackground.isEnabled = false
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
    viewLifecycleOwner.lifecycleScope.launchWhenResumed {
      extraViewEventsFlow.emit(BackClick)
    }
    return BackEffect.NO_POP
  }

  companion object {
    private const val MOTION_LAYOUT_STATE_KEY = "TRANSACTION_MOTION_LAYOUT_STATE"
    private const val FAB_ICON_STATE_KEY = "FAB_ICON_STATE_KEY"
    private const val IS_ON_CALCULATOR = "IS_ON_CALCULATOR"
    private const val IS_YEAR_MONTH_SELECTOR_ACTIVE_KEY = "IS_YEAR_MONTH_SELECTOR_ACTIVE"
  }
}
