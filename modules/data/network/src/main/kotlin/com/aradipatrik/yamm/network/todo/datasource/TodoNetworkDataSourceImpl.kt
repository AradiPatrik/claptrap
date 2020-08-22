package com.aradipatrik.yamm.network.todo.datasource

import com.aradipatrik.yamm.domain.Todo
import com.aradipatrik.yamm.domain.datasources.network.TodoNetworkDataSource
import com.aradipatrik.yamm.network.todo.api.TodoApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.util.*
import javax.inject.Inject

class TodoNetworkDataSourceImpl @Inject constructor(
  private val todoApi: TodoApi
) : TodoNetworkDataSource {
  override fun getAllTodos(): Flow<List<Todo>> = flow {
    emit(
      todoApi.getAllTodos()
        .map {
          Todo(
            id = it.id,
            name = it.name,
            isDone = it.isDone
          )
        }
    )
  }
}
