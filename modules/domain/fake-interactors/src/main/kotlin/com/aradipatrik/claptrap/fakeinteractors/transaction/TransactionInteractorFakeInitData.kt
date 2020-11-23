package com.aradipatrik.claptrap.fakeinteractors.transaction

import com.aradipatrik.claptrap.fakeinteractors.generators.CommonMockGenerator.of
import com.aradipatrik.claptrap.fakeinteractors.generators.TransactionMockGenerator.nextTransaction
import kotlin.random.Random

internal val initialTransactions = 50 of { Random.nextTransaction() }
