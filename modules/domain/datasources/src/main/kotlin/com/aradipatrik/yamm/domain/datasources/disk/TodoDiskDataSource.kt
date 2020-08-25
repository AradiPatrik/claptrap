package com.aradipatrik.yamm.domain.datasources.disk

import com.aradipatrik.yamm.domain.Todo
import kotlinx.coroutines.flow.Flow

interface TodoDiskDataSource {
  fun getAllTodos(): Flow<List<Todo>>

  suspend fun saveAll(todos: List<Todo>)
}
