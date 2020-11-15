package com.aradipatrik.claptrap.interactors.interfaces.todo

import com.aradipatrik.claptrap.domain.Todo
import kotlinx.coroutines.flow.Flow

interface TodoInteractor {
  fun getAllTodos(): Flow<List<Todo>>
  fun getTodoById(id: String): Flow<Todo>

  suspend fun setTodoDone(todo: Todo, isDone: Boolean)

  suspend fun changeNameOfTodo(todo: Todo, name: String)
}
