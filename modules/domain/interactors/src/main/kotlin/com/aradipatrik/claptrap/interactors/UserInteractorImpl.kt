package com.aradipatrik.claptrap.interactors

import com.aradipatrik.claptrap.domain.User
import com.aradipatrik.claptrap.domain.datasources.disk.UserDiskDataSource
import com.aradipatrik.claptrap.domain.datasources.network.UserNetworkDataSource
import com.aradipatrik.claptrap.interactors.interfaces.todo.UserInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class UserInteractorImpl @Inject constructor(
  private val userNetworkDataSource: UserNetworkDataSource,
  private val userDiskDataSource: UserDiskDataSource,
): UserInteractor {
  override fun getSignedInUserFlow(): Flow<User?> = userDiskDataSource.getSignedInUser()

  override suspend fun signInWithGoogleJwt(jwt: String) {
    userDiskDataSource.setToken(jwt)
    userDiskDataSource.setSignedInUser(userNetworkDataSource.signInWithGoogleToken())
  }
}
