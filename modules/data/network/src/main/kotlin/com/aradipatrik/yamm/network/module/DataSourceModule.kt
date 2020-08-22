package com.aradipatrik.yamm.network.module

import com.aradipatrik.yamm.domain.datasources.network.TodoNetworkDataSource
import com.aradipatrik.yamm.network.todo.datasource.TodoNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class DataSourceModule {
  @Binds
  abstract fun bindTodoNetworkDataSource(
    todoNetworkDataSource: TodoNetworkDataSourceImpl
  ): TodoNetworkDataSource
}
