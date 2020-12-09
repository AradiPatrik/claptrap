package com.aradipatrik.claptrap.fakeinteractors.generators

import com.aradipatrik.claptrap.domain.Category
import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.fakeinteractors.generators.CommonMockGenerator.nextDate
import com.aradipatrik.claptrap.fakeinteractors.generators.CommonMockGenerator.nextEnum
import com.aradipatrik.claptrap.fakeinteractors.generators.CommonMockGenerator.nextId
import com.aradipatrik.claptrap.fakeinteractors.generators.CommonMockGenerator.nextMoney
import com.aradipatrik.claptrap.fakeinteractors.generators.LoremIpsum.nextCapitalWord
import com.aradipatrik.claptrap.fakeinteractors.generators.LoremIpsum.nextWords
import org.joda.time.YearMonth
import kotlin.random.Random

internal object TransactionMockGenerator {
  fun Random.nextCategory() = Category(
    nextId(),
    nextCapitalWord(),
    nextEnum()
  )

  fun Random.nextTransaction() = Transaction(
    nextId(),
    nextMoney(),
    nextDate(2020..2020),
    nextWords(count = 3, capitalize = true),
    nextCategory()
  )

  fun Random.nextTransactionInYearMonth(yearMonth: YearMonth) = Transaction(
    nextId(),
    nextMoney(),
    nextDate(
      yearRange = yearMonth.year..yearMonth.year,
      monthRange = yearMonth.monthOfYear..yearMonth.monthOfYear
    ),
    nextWords(count = 3, capitalize = true),
    nextCategory()
  )
}
