package com.aradipatrik.claptrap.feature.todos.edit.model

import android.os.Bundle
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.aradipatrik.claptrap.feature.todos.edit.model.EditTodoViewState.Editing
import com.aradipatrik.claptrap.feature.todos.edit.model.EditTodoViewState.Saving
import com.aradipatrik.claptrap.interactors.todo.TodoInteractor
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EditTodoViewModel @ViewModelInject constructor(
  private val todoInteractor: TodoInteractor
) : ClaptrapViewModel<EditTodoViewState, EditTodoViewEvent>(EditTodoViewState.Loading) {
  private val paramChannel = Channel<Bundle?>()
  val viewEffects = Channel<EditTodoViewEffect>()

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
        setState<EditTodoViewState> {
          Editing(todo)
        }
      }
      .launchIn(viewModelScope)
  }

  fun setArguments(bundle: Bundle?) = viewModelScope.launch {
    paramChannel.send(bundle)
  }

  override fun processInput(viewEvent: EditTodoViewEvent) = when (viewEvent) {
    EditTodoViewEvent.DoneClick -> {
      withState<Editing> { state ->
        setState<Editing> { Saving(state.todo) }
        withState<Saving> {
          delay(1000)
          todoInteractor.changeNameOfTodo(it.todo, it.todo.name)
          viewEffects.send(EditTodoViewEffect.NavigateBack)
        }
      }
    }
    EditTodoViewEvent.BackClick -> withState<EditTodoViewState> {
      viewEffects.send(EditTodoViewEffect.NavigateBack)
    }
    is EditTodoViewEvent.NameChanged -> setState<Editing> { state ->
      Editing(state.todo.copy(name = viewEvent.newName))
    }
  }
}
