package com.aradipatrik.yamm.domain.datasources.network

import com.aradipatrik.yamm.domain.Todo
import kotlinx.coroutines.flow.Flow

interface TodoNetworkDataSource {
  fun getAllTodos(): Flow<List<Todo>>
}
