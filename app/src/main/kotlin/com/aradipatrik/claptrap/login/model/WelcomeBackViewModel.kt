package com.aradipatrik.claptrap.login.model

import androidx.hilt.lifecycle.ViewModelInject
import com.aradipatrik.claptrap.domain.User
import com.aradipatrik.claptrap.interactors.interfaces.todo.UserInteractor
import com.aradipatrik.claptrap.login.model.WelcomeBackViewEffect.NavigateToMainScreen
import com.aradipatrik.claptrap.login.model.WelcomeBackViewEffect.ShowSignInWithGoogleOAuthFlow
import com.aradipatrik.claptrap.login.model.WelcomeBackViewEvent.SignInSuccessful
import com.aradipatrik.claptrap.login.model.WelcomeBackViewEvent.SignInWithGoogle
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel

class WelcomeBackViewModel @ViewModelInject constructor(
  private val userInteractor: UserInteractor
) : ClaptrapViewModel<
  WelcomeBackViewState,
  WelcomeBackViewEvent,
  WelcomeBackViewEffect
>(WelcomeBackViewState) {
  override fun processInput(viewEvent: WelcomeBackViewEvent) = when(viewEvent) {
    is SignInWithGoogle -> startSignInWithGoogleFlow()
    is SignInSuccessful -> signIn(viewEvent.user)
  }

  private fun startSignInWithGoogleFlow() = sideEffect {
    viewEffects.emit(ShowSignInWithGoogleOAuthFlow)
  }

  private fun signIn(user: User) = sideEffect {
    userInteractor.setSignedInUser(user)
    viewEffects.emit(NavigateToMainScreen)
  }
}
