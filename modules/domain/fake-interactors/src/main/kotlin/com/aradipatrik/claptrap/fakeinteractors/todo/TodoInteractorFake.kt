package com.aradipatrik.claptrap.fakeinteractors.todo

import com.aradipatrik.claptrap.domain.Todo
import com.aradipatrik.claptrap.interactors.interfaces.todo.TodoInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoInteractorFake @Inject constructor() : TodoInteractor {
  private val todosFlow = MutableStateFlow(listOf(
    Todo("1", "Add ability to test app without BE", true),
    Todo("2", "Be really happy", false),
  ))

  override fun getAllTodos(): Flow<List<Todo>> = todosFlow
    .onEach {
      assert(true)
    }

  override fun getTodoById(id: String): Flow<Todo> = todosFlow
    .map { todos -> todos.first { it.id == id } }

  override suspend fun setTodoDone(todo: Todo, isDone: Boolean) {
    todosFlow.value = todosFlow.value.map {
      if (it.id == todo.id) {
        it.copy(isDone = isDone)
      } else {
        it
      }
    }
  }

  override suspend fun changeNameOfTodo(todo: Todo, name: String) {
    todosFlow.value = todosFlow.value.map {
      if (it.id == todo.id) {
        it.copy(name = name)
      } else {
        it
      }
    }
  }

}