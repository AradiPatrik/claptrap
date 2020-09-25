package com.aradipatrik.claptrap.feature.todos.list.model

import com.aradipatrik.claptrap.domain.Todo

sealed class TodoListViewState {
  object Loading : TodoListViewState()

  data class ListLoaded(val list: List<Todo>) : TodoListViewState()

  object Error : TodoListViewState()
}
