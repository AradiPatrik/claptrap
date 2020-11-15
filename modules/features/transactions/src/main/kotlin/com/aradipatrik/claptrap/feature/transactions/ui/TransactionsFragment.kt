package com.aradipatrik.claptrap.feature.transactions.ui

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aradipatrik.claptrap.domain.Category
import com.aradipatrik.claptrap.domain.CategoryIcon
import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.feature.transactions.R
import com.aradipatrik.claptrap.feature.transactions.databinding.FragmentTransactionsBinding
import com.aradipatrik.claptrap.feature.transactions.model.*
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.joda.money.Money
import org.joda.time.DateTime
import java.util.*
import kotlin.math.abs

@AndroidEntryPoint
class TransactionsFragment : ClapTrapFragment<
  TransactionsViewState,
  TransactionsViewEvent,
  TransactionsViewEffect,
  FragmentTransactionsBinding
  >(R.layout.fragment_transactions, FragmentTransactionsBinding::inflate) {
  override val viewModel by viewModels<TransactionsViewModel>()
  override val viewEvents: Flow<TransactionsViewEvent> = emptyFlow()

  private val transactionAdapter by lazy { TransactionAdapter(lifecycleScope) }

  private val mockDomainTransaction = Transaction(
    "", Money.parse("HUF 50"),
    DateTime.now(), "", Category(
      "", CategoryIcon.CART
    )
  )

  private val notes = listOf(
    "Starbux coffee", "Finomsag", "Schmuck", "Jysk", "Diszek", "Coop", "Spar", "Telefon"
  )

  private val transactionPresentations = generateSequence {
    TransactionPresentation(
      domain = mockDomainTransaction.copy(id = UUID.randomUUID().toString()),
      amount = (Random().nextInt() % 800).toString(),
      date = "2017 / ${Random().nextInt() % 13} / ${Random().nextInt() % 32}",
      R.drawable.ic_money,
      note = notes[abs(Random().nextInt() % (notes.size - 1))],
      "$"
    )
  }

  override fun initViews() {
    binding.transactionRecyclerView.adapter = transactionAdapter
    binding.transactionRecyclerView.layoutManager = LinearLayoutManager(context)
    transactionAdapter.submitList(
      transactionPresentations.take(20)
        .map { TransactionListItem.Item(it) }
        .toList()
    )
  }

  override fun render(viewState: TransactionsViewState) {

  }

  override fun react(viewEffect: TransactionsViewEffect) {

  }

}
