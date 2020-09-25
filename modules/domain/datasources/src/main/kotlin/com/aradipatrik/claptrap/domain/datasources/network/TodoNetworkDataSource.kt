package com.aradipatrik.claptrap.domain.datasources.network

import com.aradipatrik.claptrap.domain.Todo

interface TodoNetworkDataSource {
  suspend fun getAllTodos(): List<Todo>
}
