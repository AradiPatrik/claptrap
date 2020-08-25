package com.aradipatrik.yamm.network.todo.datasource

import com.aradipatrik.yamm.domain.Todo
import com.aradipatrik.yamm.domain.datasources.network.TodoNetworkDataSource
import com.aradipatrik.yamm.network.todo.api.TodoApi
import javax.inject.Inject

class TodoNetworkDataSourceImpl @Inject constructor(
  private val todoApi: TodoApi
) : TodoNetworkDataSource {
  override suspend fun getAllTodos() = todoApi.getAllTodos()
    .map {
      Todo(
        id = it.id,
        name = it.name,
        isDone = it.isDone
      )
    }
}
