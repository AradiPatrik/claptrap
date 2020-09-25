package com.aradipatrik.claptrap.disk.todo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aradipatrik.claptrap.disk.todo.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
  @Query("SELECT * FROM todo")
  fun getAll(): Flow<List<TodoEntity>>

  @Query("SELECT * FROM todo WHERE id = :todoId")
  fun getById(todoId: String): Flow<TodoEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(todoEntity: TodoEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(todoEntity: List<TodoEntity>)
}
