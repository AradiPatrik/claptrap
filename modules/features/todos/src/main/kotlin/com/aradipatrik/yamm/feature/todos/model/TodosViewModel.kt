package com.aradipatrik.yamm.feature.todos.model

import com.aradipatrik.yamm.interactors.todo.TodoInteractor
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class TodosViewModel @ViewModelInject constructor(
  private val todoInteractor: TodoInteractor
) : ViewModel() {

  val todosViewState = liveData {
    emit(TodosViewState.Loading)
    delay(2000)
    emitSource(
      todoInteractor.getAllTodos()
        .map { TodosViewState.ListLoaded(it) }
        .asLiveData()
    )
  }
}
