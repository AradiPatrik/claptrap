package com.aradipatrik.claptrap.interactors

import com.aradipatrik.claptrap.domain.User
import com.aradipatrik.claptrap.domain.datasources.network.UserNetworkDataSource
import com.aradipatrik.claptrap.interactors.interfaces.todo.UserInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class UserInteractorImpl @Inject constructor(
  private val userNetworkDataSource: UserNetworkDataSource
): UserInteractor {
  private val signedInUser = MutableStateFlow<User?>(null)

  override fun getSignedInUserFlow(): Flow<User?> = signedInUser

  override suspend fun signInWithGoogleCredentials(user: User) {
    userNetworkDataSource.signInWithGoogleToken(user.idToken)
    println("APDEBUG: User signed in")
  }

  override suspend fun signInWithEmailAndPassword(email: String, password: String) {
    TODO("Not yet implemented")
  }
}