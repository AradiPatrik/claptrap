package com.aradipatrik.claptrap.network.todo.datasource

import com.aradipatrik.claptrap.domain.Todo
import com.aradipatrik.claptrap.domain.datasources.network.TodoNetworkDataSource
import com.aradipatrik.claptrap.network.todo.api.TodoApi
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
