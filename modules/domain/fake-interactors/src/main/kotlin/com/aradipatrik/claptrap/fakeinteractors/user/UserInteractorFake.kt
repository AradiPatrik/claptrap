package com.aradipatrik.claptrap.fakeinteractors.user

import com.aradipatrik.claptrap.domain.User
import com.aradipatrik.claptrap.interactors.interfaces.todo.UserInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class UserInteractorFake @Inject constructor(): UserInteractor {
  private val signedInUserStateFlow = MutableStateFlow<User?>(null)

  override suspend fun signInWithGoogle(idToken: String) {
    signedInUserStateFlow.value = User(idToken, "Patrik Aradi", null)
  }

  override fun getSignedInUserFlow(): Flow<User?> = signedInUserStateFlow
}
