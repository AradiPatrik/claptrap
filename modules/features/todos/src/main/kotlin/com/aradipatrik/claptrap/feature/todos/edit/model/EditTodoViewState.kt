package com.aradipatrik.claptrap.feature.todos.edit.model

import com.aradipatrik.claptrap.domain.Todo

sealed class EditTodoViewState {
  object Loading : EditTodoViewState()

  data class Editing(
    val todo: Todo
  ) : EditTodoViewState()

  data class Saving(
    val todo: Todo
  ) : EditTodoViewState()
}
