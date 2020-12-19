package com.aradipatrik.claptrap.feature.transactions.edit.ui

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.aradipatrik.claptrap.common.backdrop.BackEffect
import com.aradipatrik.claptrap.common.backdrop.BackListener
import com.aradipatrik.claptrap.common.backdrop.FragmentExt.destinationViewModels
import com.aradipatrik.claptrap.common.backdrop.ViewDelegates.settingTextInputLayoutContent
import com.aradipatrik.claptrap.common.backdrop.backdrop
import com.aradipatrik.claptrap.feature.transactions.R
import com.aradipatrik.claptrap.feature.transactions.databinding.FragmentEditTransactionBinding
import com.aradipatrik.claptrap.feature.transactions.di.CurrencyValueMoneyFormatter
import com.aradipatrik.claptrap.feature.transactions.di.LongYearMonthDayFormatter
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewEffect
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewEvent
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewEvent.*
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewModel
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewState
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewState.Editing
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewState.Loading
import com.aradipatrik.claptrap.feature.transactions.list.ui.CategoryAdapter
import com.aradipatrik.claptrap.feature.transactions.mapper.CategoryIconMapper.drawableRes
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.theme.widget.AnimationConstants.QUICK_ANIMATION_DURATION
import com.aradipatrik.claptrap.theme.widget.ViewThemeUtil.colorPrimary
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.joda.money.format.MoneyFormatter
import org.joda.time.format.DateTimeFormatter
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.textChangeEvents
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

  private var memoText: String by settingTextInputLayoutContent {
    binding.inputsContainer.memoTextInputLayout
  }

  private var amountText: String by settingTextInputLayoutContent {
    binding.inputsContainer.amountTextInputLayout
  }

  private var categoryText: String by settingTextInputLayoutContent {
    binding.inputsContainer.categoryTextInputLayout
  }

  private var dateText: String by settingTextInputLayoutContent {
    binding.inputsContainer.dateTextInputLayout
  }

  @Inject lateinit var viewModelFactory: EditTransactionViewModel.AssistedFactory
  @Inject lateinit var categoryAdapter: CategoryAdapter.Factory

  @Inject
  @LongYearMonthDayFormatter
  lateinit var dateTimeFormatter: DateTimeFormatter

  override val viewModel by destinationViewModels<EditTransactionViewModel> {
    EditTransactionViewModel.provideFactory(viewModelFactory, transactionId)
  }

  override val viewEvents: Flow<EditTransactionViewEvent>
    get() = merge(
      binding.deleteButton.clicks().map { DeleteButtonClick },
      binding.inputsContainer.memoTextInputLayout.editText!!.textChangeEvents()
        .drop(1)
        .map { MemoChange(it.text.toString()) },
      binding.inputsContainer.amountTextInputLayout.editText!!.textChangeEvents()
        .drop(1)
        .map { AmountChange(it.text.toString()) },
      binding.editDoneFab.clicks().map { EditDoneClick }
    )

  override fun initViews(savedInstanceState: Bundle?) {
    backdrop.switchMenu(EditTransactionMenuFragment::class.java, requireArguments())
    viewsToFloatAndFadeIn.onEach { it.alpha = 0.0f }
    animateViewsIn()

    binding.inputsContainer.dateTextInputLayout.isActivated = true
  }

  private fun animateViewsIn() = lifecycleScope.launchWhenResumed {
    val overshootInterpolator = OvershootInterpolator()
    val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
    val translationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 200.0f, 0.0f)

    listOf(binding.editTransactionHeader, binding.deleteButton).map {
      ObjectAnimator.ofPropertyValuesHolder(it, alpha).start()
    }

    viewsToFloatAndFadeIn.map {
      ObjectAnimator.ofPropertyValuesHolder(it, alpha, translationY).apply {
        duration = QUICK_ANIMATION_DURATION
        interpolator = overshootInterpolator
      }.start()

      delay(QUICK_STAGGER_DURATION)
    }

    binding.editDoneFab.show()
  }

  override fun render(viewState: EditTransactionViewState) = when (viewState) {
    is Loading -> {
    }
    is Editing -> renderEditingState(viewState)
  }

  private fun renderEditingState(editing: Editing) = with(editing) {
    amountText = amount
    memoText = memo
    categoryText = category.name
    dateText = date.toString(dateTimeFormatter)

    binding.inputsContainer.categoryTextInputLayout.startIconDrawable =
      ContextCompat.getDrawable(requireContext(), category.icon.drawableRes)
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

  private val viewsToFloatAndFadeIn
    get() = listOf(
      binding.inputsContainer.memoTextInputLayout,
      binding.inputsContainer.amountTextInputLayout,
      binding.inputsContainer.dateTextInputLayout,
      binding.inputsContainer.categoryTextInputLayout
    )
}

private const val QUICK_STAGGER_DURATION = 100L