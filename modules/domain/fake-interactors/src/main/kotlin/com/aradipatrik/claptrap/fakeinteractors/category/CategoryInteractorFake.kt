package com.aradipatrik.claptrap.fakeinteractors.category

import com.aradipatrik.claptrap.fakeinteractors.generators.CommonMockGenerator.of
import com.aradipatrik.claptrap.fakeinteractors.generators.TransactionMockGenerator.nextCategory
import com.aradipatrik.claptrap.interactors.interfaces.todo.CategoryInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

class CategoryInteractorFake : CategoryInteractor {
  private val categories = MutableStateFlow(initialCategories)

  override fun getAllCategories() = categories
}

internal val initialCategories = 50 of { Random.nextCategory() }
