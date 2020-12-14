package com.aradipatrik.claptrap.feature.transactions.edit.ui

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import com.aradipatrik.claptrap.common.backdrop.FragmentExt.destinationViewModels
import com.aradipatrik.claptrap.common.backdrop.backdrop
import com.aradipatrik.claptrap.common.backdrop.menuBackDrop
import com.aradipatrik.claptrap.feature.transactions.R
import com.aradipatrik.claptrap.feature.transactions.mapper.CategoryIconMapper.drawableRes
import com.aradipatrik.claptrap.feature.transactions.databinding.FragmentEditTransactionBinding
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewEffect
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewEvent
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewModel
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewState
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewState.Editing
import com.aradipatrik.claptrap.feature.transactions.edit.model.EditTransactionViewState.Loading
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.joda.money.format.MoneyAmountStyle
import org.joda.money.format.MoneyFormatterBuilder
import org.joda.time.format.DateTimeFormat
import javax.inject.Inject

@AndroidEntryPoint
class EditTransactionFragment :
  ClapTrapFragment<EditTransactionViewState, EditTransactionViewEvent, EditTransactionViewEffect, FragmentEditTransactionBinding>(
    R.layout.fragment_edit_transaction, FragmentEditTransactionBinding::inflate
  ) {
  private val transactionId by lazy {
    requireArguments().getString("transactionId") ?: error("transactionId is required")
  }

  @Inject
  lateinit var viewModelFactory: EditTransactionViewModel.AssistedFactory

  override val viewModel by destinationViewModels<EditTransactionViewModel> {
    EditTransactionViewModel.provideFactory(viewModelFactory, transactionId)
  }

  override val viewEvents: Flow<EditTransactionViewEvent> get() = emptyFlow()

  override fun initViews(savedInstanceState: Bundle?) {
    backdrop.switchMenu(EditTransactionMenuFragment::class.java, requireArguments())
  }

  override fun render(viewState: EditTransactionViewState) = when (viewState) {
    is Loading -> {
    }
    is Editing -> renderEditingState(viewState)
  }

  private fun renderEditingState(editing: Editing) = with(editing.transaction) {
    val moneyFormatter = MoneyFormatterBuilder()
      .appendCurrencySymbolLocalized()
      .appendLiteral(" ")
      .appendAmount(MoneyAmountStyle.LOCALIZED_GROUPING)
      .toFormatter()

    val dateTimeFormatter = DateTimeFormat.forStyle("M-")

    binding.amountTextInputLayout.editText!!.setText(moneyFormatter.print(money))

    binding.categoryTextInputLayout.editText!!.setText(category.name)
    binding.categoryTextInputLayout.startIconDrawable =
      ContextCompat.getDrawable(requireContext(), category.icon.drawableRes)

    binding.dateTextInputLayout.editText!!.setText(date.toString(dateTimeFormatter))

    binding.memoTextInputLayout.editText!!.setText(note)
  }

  override fun react(viewEffect: EditTransactionViewEffect) {
  }
}