package com.aradipatrik.claptrap.disk.todo.datasource

import com.aradipatrik.claptrap.domain.Todo
import com.aradipatrik.claptrap.disk.todo.dao.TodoDao
import com.aradipatrik.claptrap.disk.todo.entity.TodoEntity
import com.aradipatrik.claptrap.domain.datasources.disk.TodoDiskDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoDiskDataSourceImpl @Inject constructor(
  private val todoDao: TodoDao
) : TodoDiskDataSource {
  override fun getAllTodos(): Flow<List<Todo>> = todoDao.getAll()
    .map { todos ->
      todos.map {
        Todo(
          id = it.id,
          name = it.name,
          isDone = it.isDone
        )
      }
    }

  override fun getById(id: String): Flow<Todo> = todoDao.getById(id)
    .map { todo ->
      todo.let {
        Todo(
          id = it.id,
          name = it.name,
          isDone = it.isDone
        )
      }
    }

  override suspend fun saveAll(todos: List<Todo>) {
    todoDao.insertAll(
      todos.map {
        TodoEntity(
          id = it.id,
          name = it.name,
          isDone = it.isDone
        )
      }
    )
  }

  override suspend fun save(todo: Todo) {
    todoDao.insert(
      todo.let {
        TodoEntity(
          id = it.id,
          name = it.name,
          isDone = it.isDone
        )
      }
    )
  }
}
