package com.aradipatrik.claptrap.feature.todos.edit.model

sealed class EditTodoViewEvent {
  object DoneClick: EditTodoViewEvent()
  object BackClick: EditTodoViewEvent()
  data class NameChanged(val newName: String): EditTodoViewEvent()
}
