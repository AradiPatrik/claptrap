package com.aradipatrik.claptrap.network.module

import com.aradipatrik.claptrap.domain.datasources.network.BearerTokenHolder
import com.aradipatrik.claptrap.domain.datasources.network.TodoNetworkDataSource
import com.aradipatrik.claptrap.domain.datasources.network.UserNetworkDataSource
import com.aradipatrik.claptrap.network.todo.datasource.TodoNetworkDataSourceImpl
import com.aradipatrik.claptrap.network.todo.datasource.UserNetworkDataSourceImpl
import com.aradipatrik.claptrap.network.todo.holder.BearerTokenHolderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class DataSourceModule {
  @Binds
  @Singleton
  abstract fun bindTodoNetworkDataSource(
    todoNetworkDataSource: TodoNetworkDataSourceImpl
  ): TodoNetworkDataSource

  @Binds
  @Singleton
  abstract fun bindUserNetworkDataSource(
    dataSource: UserNetworkDataSourceImpl
  ): UserNetworkDataSource

  @Binds
  @Singleton
  abstract fun bindBearerTokenHolder(
    holder: BearerTokenHolderImpl
  ): BearerTokenHolder
}
