package com.aradipatrik.yamm.network.todo.api

import com.aradipatrik.yamm.apimodels.TodoApiModel
import retrofit2.http.GET

interface TodoApi {
  @GET("todos")
  suspend fun getAllTodos(): List<TodoApiModel>
}
