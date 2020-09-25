package com.aradipatrik.claptrap.feature.todos.list.model

import com.aradipatrik.claptrap.domain.Todo

sealed class TodoListViewEffect {
  data class NavigateToEdit(val todo: Todo): TodoListViewEffect()
}
