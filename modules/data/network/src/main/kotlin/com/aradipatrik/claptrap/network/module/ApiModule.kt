package com.aradipatrik.claptrap.network.module

import com.aradipatrik.claptrap.network.todo.api.TodoApi
import com.aradipatrik.claptrap.network.todo.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApiModule {
  @Provides
  @Singleton
  fun provideTodoApi(retrofit: Retrofit): TodoApi = retrofit.create(TodoApi::class.java)

  @Provides
  @Singleton
  fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)
}
