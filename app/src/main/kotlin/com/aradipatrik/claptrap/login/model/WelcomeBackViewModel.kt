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
>(WelcomeBackViewState()) {
  override fun processInput(viewEvent: WelcomeBackViewEvent) = when(viewEvent) {
    is SignInWithGoogle -> startSignInWithGoogleFlow()
    is SignInSuccessful -> signInWithGoogle(viewEvent.user)
    is WelcomeBackViewEvent.EmailTextChange -> changeEmail(viewEvent.email)
    is WelcomeBackViewEvent.PasswordTextChange -> changePassword(viewEvent.password)
    is WelcomeBackViewEvent.SignInWithEmailAndPassword -> signInWithEmailAndPassword()
  }

  private fun changeEmail(email: String) = reduceState { state ->
    state.copy(email = email)
  }

  private fun changePassword(password: String) = reduceState { state ->
    state.copy(password = password)
  }

  private fun startSignInWithGoogleFlow() = sideEffect {
    viewEffects.emit(ShowSignInWithGoogleOAuthFlow)
  }

  private fun signInWithGoogle(user: User) = sideEffect {
    userInteractor.signInWithGoogleCredentials(user)
    viewEffects.emit(NavigateToMainScreen)
  }

  private fun signInWithEmailAndPassword() = sideEffect { state ->
    userInteractor.signInWithEmailAndPassword(state.email, state.password)
    viewEffects.emit(NavigateToMainScreen)
  }
}
