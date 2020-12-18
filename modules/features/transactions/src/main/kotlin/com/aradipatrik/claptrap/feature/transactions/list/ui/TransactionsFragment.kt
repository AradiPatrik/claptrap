package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.os.bundleOf
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
  @Inject lateinit var categoryAdapterFactory: CategoryAdapter.Factory

  override val viewModel by activityViewModels<TransactionsViewModel>()

  private val transactionAdapter by lazy { transactionAdapterFactory.create(lifecycleScope) }
  private val categoryAdapter by lazy { categoryAdapterFactory.create(lifecycleScope) }

  override val viewEvents: Flow<TransactionsViewEvent>
    get() = merge(
      binding.fabBackground.clicks().map { ActionClick },
      categoryAdapter.categorySelectedEvents.map { CategorySelected(it.category) },
      transactionAdapter.viewEvents
    )

  private val checkToEquals by lazy { getAnimatedVectorDrawable(R.drawable.check_to_equals) }
  private val equalsToCheck by lazy { getAnimatedVectorDrawable(R.drawable.equals_to_check) }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    postponeEnterTransition()
    binding.transactionRecyclerView.viewTreeObserver.addOnPreDrawListener {
      startPostponedEnterTransition()
      true
    }

    binding.transactionRecyclerView.layoutManager = LinearLayoutManager(context)
    binding.transactionRecyclerView.adapter = transactionAdapter
  }

  @SuppressLint("BinaryOperationInTimber")
  override fun initViews(savedInstanceState: Bundle?) {
    transactionAdapter.headerChangeEvents
      .onEach(binding.transactionsHeader::text::set)
      .launchInWhenResumed(lifecycleScope)

    if (savedInstanceState != null && savedInstanceState.containsViewState()) {

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

  override fun saveViewState(outState: Bundle) {
    outState.putBoolean(FAB_ICON_STATE_KEY, binding.fabIcon.isAtStartState)
    outState.putBoolean(
      IS_ON_CALCULATOR, binding.fabIcon.startToEndAnimatedVectorDrawable == checkToEquals
    )
  }

  override fun render(viewState: TransactionsViewState) = when (viewState) {
    is TransactionsViewState.Loading -> renderLoading()
    is TransactionsViewState.TransactionsLoaded -> renderLoaded(viewState)
    is TransactionsViewState.Adding -> renderAdding(viewState)
  }

  private fun renderAdding(viewState: TransactionsViewState.Adding) {
    categoryAdapter.submitList(viewState.categories.map {
      CategoryListItem(it, it.id == viewState.selectedCategory?.id)
    })
  }

  private fun renderLoading() {
  }

  private fun renderLoaded(viewState: TransactionsViewState.TransactionsLoaded) {
    transactionAdapter.submitList(
      transactionListBuilderDelegate.generateListItemsFrom(viewState.transactions)
    )
  }

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
      clickedView.root to clickedView.root.transitionName
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

  private fun showDatePicker() = lifecycleScope.launchWhenResumed {
    val selectedDateInstant = MaterialDatePicker.Builder
      .datePicker()
      .build()
      .showAndWaitWith(childFragmentManager)

    extraViewEventsFlow.emit(DateSelected(DateTime(selectedDateInstant)))
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
