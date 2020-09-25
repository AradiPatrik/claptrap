package com.aradipatrik.claptrap.disk.module

import com.aradipatrik.claptrap.disk.todo.datasource.TodoDiskDataSourceImpl
import com.aradipatrik.claptrap.domain.datasources.disk.TodoDiskDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class DataSourceModule {
  @Binds
  abstract fun bindTodoDiskDataSource(
    todoDiskDataSource: TodoDiskDataSourceImpl
  ): TodoDiskDataSource
}
