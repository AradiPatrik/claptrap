package com.aradipatrik.yamm.feature.todos.model

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.aradipatrik.yamm.domain.Todo
import kotlinx.coroutines.delay
import java.util.*

class TodosViewModel @ViewModelInject constructor() : ViewModel() {
  val todosViewState = liveData {
    emit(TodosViewState.Loading)
    delay(2000)
    emit(
      TodosViewState.ListLoaded(
        listOf(
          Todo("Vidd ki a szemetet", UUID.randomUUID().toString(), isDone = true),
          Todo("Fozz vacsorat", UUID.randomUUID().toString(), isDone = true),
          Todo("Tanulj nemetul", UUID.randomUUID().toString(), isDone = false),
          Todo("Kodoljal", UUID.randomUUID().toString(), isDone = true),
        )
      )
    )
  }
}
