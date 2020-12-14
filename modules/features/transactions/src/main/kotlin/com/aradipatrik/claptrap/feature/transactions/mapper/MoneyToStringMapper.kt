package com.aradipatrik.claptrap.feature.transactions.mapper

import com.aradipatrik.claptrap.feature.transactions.di.CurrencyValueMoneyFormatter
import com.aradipatrik.claptrap.feature.transactions.di.ValueMoneyFormatter
import org.joda.money.Money
import org.joda.money.format.MoneyFormatter
import javax.inject.Inject

class MoneyToStringMapper @Inject constructor(
  @ValueMoneyFormatter private val valueOnlyMoneyFormatter: MoneyFormatter,
  @CurrencyValueMoneyFormatter private val currencyValueMoneyFormatter: MoneyFormatter
) {
  fun mapValueOnly(money: Money) = valueOnlyMoneyFormatter.print(money)

  fun mapWithCurrency(money: Money) = currencyValueMoneyFormatter.print(money)
}
