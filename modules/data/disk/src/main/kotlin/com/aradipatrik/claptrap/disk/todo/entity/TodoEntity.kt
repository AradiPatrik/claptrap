package com.aradipatrik.claptrap.disk.todo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class TodoEntity(
  @PrimaryKey
  val id: String,
  @ColumnInfo(name = "name")
  val name: String,
  @ColumnInfo(name = "is_done")
  val isDone: Boolean
)
