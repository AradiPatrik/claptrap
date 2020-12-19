package com.aradipatrik.claptrap.feature.transactions.mapper

import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.feature.transactions.mapper.CategoryIconMapper.drawableRes
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionPresentation
import java.util.*
import javax.inject.Inject

class TransactionPresentationMapper @Inject constructor(
  private val dateToStringMapper: DateToStringMapper,
  private val moneyToStringMapper: MoneyToStringMapper
) {
  fun map(transaction: Transaction) = TransactionPresentation(
    domain = transaction,
    amount = moneyToStringMapper.mapValueOnly(transaction.money),
    date = dateToStringMapper.mapMediumYearMonthDay(transaction.date),
    categoryIcon = transaction.category.icon.drawableRes,
    note = if (transaction.note.isNotBlank()) {
      transaction.note
    } else {
      transaction.category.name
    },
    currencySymbol = transaction.money.currencyUnit.getSymbol(Locale.getDefault())
  )
}