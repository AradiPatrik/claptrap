package com.aradipatrik.claptrap.interactors.todo

import com.aradipatrik.claptrap.domain.Todo
import com.aradipatrik.claptrap.domain.datasources.disk.TodoDiskDataSource
import com.aradipatrik.claptrap.domain.datasources.network.TodoNetworkDataSource
import com.aradipatrik.claptrap.interactors.interfaces.todo.TodoInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoInteractorImpl @Inject constructor(
  private val networkDataSource: TodoNetworkDataSource,
  private val diskDataSource: TodoDiskDataSource
) : TodoInteractor {
  override fun getAllTodos(): Flow<List<Todo>> = flow {
    coroutineScope {
      launch {
        val todosFromNetwork = networkDataSource.getAllTodos()
        diskDataSource.saveAll(todosFromNetwork)
      }
      emitAll(diskDataSource.getAllTodos())
    }
  }.flowOn(Dispatchers.IO)

  override fun getTodoById(id: String): Flow<Todo> = diskDataSource.getById(id)

  override suspend fun setTodoDone(todo: Todo, isDone: Boolean) = withContext(Dispatchers.IO) {
    diskDataSource.save(todo.copy(isDone = isDone))
  }

  override suspend fun changeNameOfTodo(todo: Todo, name: String) {
    diskDataSource.save(todo.copy(name = name))
  }
}
