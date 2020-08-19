package com.aradipatrik.yamm.feature.todos.model

import com.aradipatrik.yamm.domain.Todo

sealed class TodosViewState {
  object Loading: TodosViewState()

  data class ListLoaded(val list: List<Todo>): TodosViewState()

  object Error: TodosViewState()
}
