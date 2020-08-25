package com.aradipatrik.yamm.interactors.todo

import com.aradipatrik.yamm.domain.Todo
import com.aradipatrik.yamm.domain.datasources.disk.TodoDiskDataSource
import com.aradipatrik.yamm.domain.datasources.network.TodoNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoInteractor @Inject constructor(
  private val networkDataSource: TodoNetworkDataSource,
  private val diskDataSource: TodoDiskDataSource
) {
  fun getAllTodos(): Flow<List<Todo>> = flow {
    val todosFromNetwork = networkDataSource.getAllTodos()
    emit(todosFromNetwork)
    diskDataSource.saveAll(todosFromNetwork)
    emitAll(diskDataSource.getAllTodos())
  }.flowOn(Dispatchers.IO)
}
