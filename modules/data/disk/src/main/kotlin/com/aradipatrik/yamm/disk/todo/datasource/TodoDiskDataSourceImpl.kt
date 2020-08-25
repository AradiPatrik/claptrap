package com.aradipatrik.yamm.disk.todo.datasource

import com.aradipatrik.yamm.domain.Todo
import com.aradipatrik.yamm.domain.datasources.disk.TodoDiskDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TodoDiskDataSourceImpl @Inject constructor() : TodoDiskDataSource {
  private val todos = mutableListOf<Todo>()

  override fun getAllTodos(): Flow<List<Todo>> = flow {
    while (true) {
      delay(1000)
      emit(todos)
    }
  }
    .distinctUntilChanged()

  override suspend fun saveAll(todos: List<Todo>) {
    this.todos.addAll(todos.map { it.copy(name = "From db: ${it.name}") })
  }

}
