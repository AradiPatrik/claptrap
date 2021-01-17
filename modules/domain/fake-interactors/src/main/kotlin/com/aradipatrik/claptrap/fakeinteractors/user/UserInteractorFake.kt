package com.aradipatrik.claptrap.fakeinteractors.user

import com.aradipatrik.claptrap.domain.User
import com.aradipatrik.claptrap.interactors.interfaces.todo.UserInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInteractorFake @Inject constructor(): UserInteractor {
  private val signedInUserStateFlow = MutableStateFlow<User?>(null)

  override suspend fun setSignedInUser(user: User) {
    signedInUserStateFlow.value = user
  }

  override fun getSignedInUserFlow(): Flow<User?> = signedInUserStateFlow
}
