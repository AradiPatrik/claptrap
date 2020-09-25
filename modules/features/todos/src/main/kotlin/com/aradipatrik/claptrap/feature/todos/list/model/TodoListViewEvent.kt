package com.aradipatrik.claptrap.feature.todos.list.model

import com.aradipatrik.claptrap.domain.Todo

sealed class TodoListViewEvent {
  data class TodoChecked(val todo: Todo, val isChecked: Boolean): TodoListViewEvent()
  data class TodoClicked(val todo: Todo): TodoListViewEvent()
}
