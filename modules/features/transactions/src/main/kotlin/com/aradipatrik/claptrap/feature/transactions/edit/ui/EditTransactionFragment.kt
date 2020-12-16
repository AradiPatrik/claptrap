package com.aradipatrik.claptrap.feature.transactions.edit.ui

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.transition.TransitionInflater
import com.aradipatrik.claptrap.common.backdrop.BackEffect
import com.aradipatrik.claptrap.common.backdrop.BackListener
import com.aradipatrik.claptrap.common.backdrop.FragmentExt.destinationViewModels
import com.aradipatrik.claptrap.common.backdrop.backdrop
import com.aradipatrik.claptrap.feature.transactions.R
import com.aradipatrik.claptrap.feature.transactions.databinding.FragmentEditTransactionBinding
import com.aradipatrik.claptrap.feature.transactions.di.CurrencyValueMoneyFormatter
import com.aradipatrik.claptrap.feature.transactions.di.LongYearMonthDayFormatter
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewEffect
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewEvent
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewEvent.BackClick
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewModel
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewState
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewState.Editing
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewState.Loading
import com.aradipatrik.claptrap.feature.transactions.list.ui.CategoryAdapter
import com.aradipatrik.claptrap.feature.transactions.mapper.CategoryIconMapper.drawableRes
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.theme.widget.MotionUtil.awaitEnd
import com.aradipatrik.claptrap.theme.widget.MotionUtil.onTransitionEnd
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.joda.money.format.MoneyFormatter
import org.joda.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class EditTransactionFragment : ClapTrapFragment<
  EditTransactionViewState,
  EditTransactionViewEvent,
  EditTransactionViewEffect,
  FragmentEditTransactionBinding
  >(
  R.layout.fragment_edit_transaction, FragmentEditTransactionBinding::inflate
), BackListener {
  private val transactionId by lazy {
    requireArguments().getString("transactionId") ?: error("transactionId is required")
  }

  @Inject
  lateinit var viewModelFactory: EditTransactionViewModel.AssistedFactory
  @Inject lateinit var categoryAdapter: CategoryAdapter.Factory

  @Inject
  @LongYearMonthDayFormatter
  lateinit var dateTimeFormatter: DateTimeFormatter

  @Inject
  @CurrencyValueMoneyFormatter
  lateinit var moneyFormatter: MoneyFormatter

  override val viewModel by destinationViewModels<EditTransactionViewModel> {
    EditTransactionViewModel.provideFactory(viewModelFactory, transactionId)
  }

  override val viewEvents: Flow<EditTransactionViewEvent> get() = emptyFlow()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      sharedElementEnterTransition = TransitionInflater.from(context)
        .inflateTransition(android.R.transition.move)
        .onTransitionEnd { fadeInInputs() }
    }
  }

  override fun initViews(savedInstanceState: Bundle?) {
    backdrop.switchMenu(EditTransactionMenuFragment::class.java, requireArguments())
    binding.frontLayer.transitionName = transactionId
    if (savedInstanceState == null) {
      viewsToFadeIn.onEach { it.alpha = 0.0f }
    } else {
      binding.editDoneFab.show()
    }
  }

  private fun fadeInInputs() = lifecycleScope.launchWhenResumed {
    val animator = ValueAnimator.ofFloat(0.0f, 1.0f).apply {
      duration = 600
      addUpdateListener { animator ->
        val animatedValue = animator.animatedValue as Float
        viewsToFadeIn.onEach { it.alpha = animatedValue }
        binding.headerSeparator.alpha = animatedValue * 0.333f
        binding.inputScrollView.translationY = 200.0f - 200.0f * animatedValue
      }
      interpolator = FastOutSlowInInterpolator()
      start()
    }
    animator.awaitEnd()
    binding.editDoneFab.show()
  }

  override fun render(viewState: EditTransactionViewState) = when (viewState) {
    is Loading -> {
    }
    is Editing -> renderEditingState(viewState)
  }

  private fun renderEditingState(editing: Editing) = with(editing.transaction) {
    binding.inputsContainer.amountTextInputLayout.editText!!.setText(moneyFormatter.print(money))

    binding.inputsContainer.categoryTextInputLayout.editText!!.setText(category.name)
    binding.inputsContainer.categoryTextInputLayout.startIconDrawable =
      ContextCompat.getDrawable(requireContext(), category.icon.drawableRes)
    binding.inputsContainer.categoryTextInputLayout.isActivated = true

    binding.inputsContainer.dateTextInputLayout.editText!!.setText(date.toString(dateTimeFormatter))
    binding.inputsContainer.dateTextInputLayout.isActivated = true

    binding.inputsContainer.memoTextInputLayout.editText!!.setText(note)
  }

  override fun react(viewEffect: EditTransactionViewEffect) = when (viewEffect) {
    EditTransactionViewEffect.Back -> goBack()
  }

  private fun goBack() {
    backdrop.clearMenu()
    backdrop.backdropNavController.popBackStack()
  }

  override fun onBack(): BackEffect {
    lifecycleScope.launchWhenResumed { extraViewEventsFlow.emit(BackClick) }
    return BackEffect.NO_POP
  }

  private val viewsToFadeIn
    get() = listOf(
      binding.inputsContainer.amountTextInputLayout,
      binding.inputsContainer.categoryTextInputLayout,
      binding.inputsContainer.dateTextInputLayout,
      binding.inputsContainer.memoTextInputLayout,
      binding.headerSeparator,
      binding.editTransactionHeader
    )
}