package com.aradipatrik.claptrap.feature.todos.list.model

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.aradipatrik.claptrap.domain.Todo
import com.aradipatrik.claptrap.feature.todos.list.model.TodoListViewEffect.NavigateToEdit
import com.aradipatrik.claptrap.feature.todos.list.model.TodoListViewEvent.TodoChecked
import com.aradipatrik.claptrap.feature.todos.list.model.TodoListViewEvent.TodoClicked
import com.aradipatrik.claptrap.feature.todos.list.model.TodoListViewState.ListLoaded
import com.aradipatrik.claptrap.interactors.interfaces.todo.TodoInteractor
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TodoListViewModel @ViewModelInject constructor(
  private val todoInteractor: TodoInteractor,
) : ClaptrapViewModel<TodoListViewState, TodoListViewEvent>(TodoListViewState.Loading) {
  val viewEffects = Channel<TodoListViewEffect>(BUFFERED)

  init {
    todoInteractor.getAllTodos()
      .onEach { todo ->
        setState<TodoListViewState> {
          ListLoaded(todo)
        }
      }
      .launchIn(viewModelScope)
  }

  override fun processInput(viewEvent: TodoListViewEvent) = withState<ListLoaded> {
    when(viewEvent) {
      is TodoChecked -> todoInteractor.setTodoDone(viewEvent.todo, viewEvent.isChecked)
      is TodoClicked -> navigateToEdit(viewEvent.todo)
    }
  }

  private fun navigateToEdit(todo: Todo) = viewModelScope.launch {
    viewEffects.send(NavigateToEdit(todo))
  }
}
