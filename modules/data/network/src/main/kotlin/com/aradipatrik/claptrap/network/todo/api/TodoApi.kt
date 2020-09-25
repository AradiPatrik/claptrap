package com.aradipatrik.claptrap.network.todo.api

import com.aradipatrik.claptrap.apimodels.TodoApiModel
import retrofit2.http.GET

interface TodoApi {
  @GET("todos")
  suspend fun getAllTodos(): List<TodoApiModel>
}
