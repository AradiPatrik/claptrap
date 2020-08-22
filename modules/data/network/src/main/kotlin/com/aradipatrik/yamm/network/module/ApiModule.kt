package com.aradipatrik.yamm.network.module

import com.aradipatrik.yamm.network.todo.api.TodoApi
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
}
