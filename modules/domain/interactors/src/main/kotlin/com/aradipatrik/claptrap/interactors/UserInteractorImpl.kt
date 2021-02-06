package com.aradipatrik.claptrap.interactors

import com.aradipatrik.claptrap.domain.User
import com.aradipatrik.claptrap.domain.datasources.network.BearerTokenHolder
import com.aradipatrik.claptrap.domain.datasources.network.UserNetworkDataSource
import com.aradipatrik.claptrap.interactors.interfaces.todo.UserInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class UserInteractorImpl @Inject constructor(
  private val userNetworkDataSource: UserNetworkDataSource,
  private val bearerTokenHolder: BearerTokenHolder
): UserInteractor {
  private val signedInUser = MutableStateFlow<User?>(null)

  override fun getSignedInUserFlow(): Flow<User?> = signedInUser

  override suspend fun signInWithGoogleJwt(jwt: String) {
    bearerTokenHolder.setToken(jwt)
    userNetworkDataSource.signInWithGoogleToken(jwt)
  }
}
