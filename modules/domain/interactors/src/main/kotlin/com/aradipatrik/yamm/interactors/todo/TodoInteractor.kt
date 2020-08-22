package com.aradipatrik.yamm.interactors.todo

import com.aradipatrik.yamm.domain.Todo
import com.aradipatrik.yamm.domain.datasources.network.TodoNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TodoInteractor @Inject constructor(
  private val networkDataSource: TodoNetworkDataSource
) {
  fun getAllTodos(): Flow<List<Todo>> = networkDataSource.getAllTodos()
}
