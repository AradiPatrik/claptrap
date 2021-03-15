package com.aradipatrik.claptrap.domain.datasources.disk

import com.aradipatrik.claptrap.domain.User
import kotlinx.coroutines.flow.Flow

interface UserDiskDataSource {
  suspend fun setToken(token: String)

  fun peakToken(): String?

  suspend fun setSignedInUser(user: User)

  fun getSignedInUser(): Flow<User?>
}
