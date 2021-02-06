package com.aradipatrik.claptrap.fakeinteractors.user

import com.aradipatrik.claptrap.domain.User
import com.aradipatrik.claptrap.interactors.interfaces.todo.UserInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInteractorFake @Inject constructor() : UserInteractor {
  private val signedInUserStateFlow = MutableStateFlow<User?>(null)

  override suspend fun signInWithGoogleCredentials(user: User) {
    signedInUserStateFlow.value = user
  }

  override suspend fun signInWithEmailAndPassword(email: String, password: String) {
    signedInUserStateFlow.value = User(UUID.randomUUID().toString(), email, null, null, null)
  }

  override fun getSignedInUserFlow(): Flow<User?> = signedInUserStateFlow
}
