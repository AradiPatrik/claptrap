package com.aradipatrik.claptrap.feature.todos.edit.model

import android.os.Bundle
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.aradipatrik.claptrap.feature.todos.edit.model.EditTodoViewState.Editing
import com.aradipatrik.claptrap.feature.todos.edit.model.EditTodoViewState.Saving
import com.aradipatrik.claptrap.interactors.interfaces.todo.TodoInteractor
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EditTodoViewModel @ViewModelInject constructor(
  private val todoInteractor: TodoInteractor
) : ClaptrapViewModel<EditTodoViewState, EditTodoViewEvent, EditTodoViewEffect>(
  EditTodoViewState.Loading
) {
  private val paramChannel = Channel<Bundle?>()

  init {
    viewModelScope.launch {
      paramChannel.receive()?.getString("todoId")?.let { editedTodoId ->
        subscribeToEditedTodoStateChanges(editedTodoId)
      } ?: error("EditTodoViewModel requires \"todoId\" argument")
    }
  }

  private fun subscribeToEditedTodoStateChanges(todoId: String) {
    todoInteractor.getTodoById(todoId)
      .onEach { todo ->
        setState { Editing(todo) }
      }
      .launchIn(viewModelScope)
  }

  fun setArguments(bundle: Bundle?) = viewModelScope.launch {
    paramChannel.send(bundle)
  }

  override fun processInput(viewEvent: EditTodoViewEvent) = when (viewEvent) {
    EditTodoViewEvent.DoneClick -> {
      reduceState<Editing> { oldState -> Saving(oldState.todo) }
      withState<Saving> {
        todoInteractor.changeNameOfTodo(it.todo, it.todo.name)
        viewEffects.send(EditTodoViewEffect.NavigateBack)
      }
    }
    EditTodoViewEvent.BackClick -> withState<EditTodoViewState> {
      viewEffects.send(EditTodoViewEffect.NavigateBack)
    }
    is EditTodoViewEvent.NameChanged -> reduceState<Editing> { oldState ->
      Editing(oldState.todo.copy(name = viewEvent.newName))
    }
  }
}
