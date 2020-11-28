package com.aradipatrik.claptrap.fakeinteractors.generators

import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.joda.time.DateTime
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

internal object CommonMockGenerator {

  internal fun Random.nextDate(
    yearRange: IntRange
  ) = DateTime(
    nextInt(yearRange),
    nextInt(1, 13),
    nextInt(1, 29),
    nextInt(0, 24),
    nextInt(0, 60)
  )

  internal fun Random.nextMoney(
    currencyUnit: CurrencyUnit = CurrencyUnit.USD,
    from: Int = 50,
    until: Int = 1000
  ) = Money.of(
    currencyUnit,
    nextInt(from, until).toDouble()
  )

  internal fun Random.nextId() = UUID.randomUUID().toString()

  internal inline fun <reified T: Enum<*>> Random.nextEnum() = enumValues<T>()
    .toList()
    .shuffled()
    .first()

  internal infix fun <T> Int.of(generatorFunction: () -> T) = generateSequence {
    generatorFunction()
  }
    .take(this)
    .toList()
}