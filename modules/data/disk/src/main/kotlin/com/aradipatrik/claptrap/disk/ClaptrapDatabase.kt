package com.aradipatrik.claptrap.disk

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aradipatrik.claptrap.disk.todo.dao.TodoDao
import com.aradipatrik.claptrap.disk.todo.entity.TodoEntity

@Database(entities = [TodoEntity::class], version = 1)
abstract class ClaptrapDatabase : RoomDatabase() {
  abstract fun todoDao(): TodoDao
}
