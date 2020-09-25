package com.aradipatrik.claptrap.domain.datasources.disk

import com.aradipatrik.claptrap.domain.Todo
import kotlinx.coroutines.flow.Flow

interface TodoDiskDataSource {
  fun getAllTodos(): Flow<List<Todo>>

  fun getById(id: String): Flow<Todo>

  suspend fun saveAll(todos: List<Todo>)

  suspend fun save(todo: Todo)
}
