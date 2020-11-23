package com.aradipatrik.claptrap.fakeinteractors.generators

import com.aradipatrik.claptrap.domain.Category
import com.aradipatrik.claptrap.domain.Transaction
import com.aradipatrik.claptrap.fakeinteractors.generators.CommonMockGenerator.nextDate
import com.aradipatrik.claptrap.fakeinteractors.generators.CommonMockGenerator.nextEnum
import com.aradipatrik.claptrap.fakeinteractors.generators.CommonMockGenerator.nextId
import com.aradipatrik.claptrap.fakeinteractors.generators.CommonMockGenerator.nextMoney
import com.aradipatrik.claptrap.fakeinteractors.generators.LoremIpsum.nextCapitalWord
import com.aradipatrik.claptrap.fakeinteractors.generators.LoremIpsum.nextWords
import kotlin.random.Random

internal object TransactionMockGenerator {
  fun Random.nextCategory() = Category(
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
}
