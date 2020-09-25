package com.aradipatrik.claptrap.network.module

import com.aradipatrik.claptrap.domain.datasources.network.TodoNetworkDataSource
import com.aradipatrik.claptrap.network.todo.datasource.TodoNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class DataSourceModule {
  @Binds
  @Singleton
  abstract fun bindTodoNetworkDataSource(
    todoNetworkDataSource: TodoNetworkDataSourceImpl
  ): TodoNetworkDataSource
}
