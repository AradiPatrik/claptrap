package com.aradipatrik.claptrap.interactors.interfaces.todo

import com.aradipatrik.claptrap.domain.User
import kotlinx.coroutines.flow.Flow

interface UserInteractor {
  fun getSignedInUserFlow(): Flow<User?>
  suspend fun signInWithGoogleCredentials(user: User)
  suspend fun signInWithEmailAndPassword(email: String, password: String)
}