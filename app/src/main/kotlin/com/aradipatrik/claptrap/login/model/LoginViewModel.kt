package com.aradipatrik.claptrap.login.model

import com.aradipatrik.claptrap.mvi.ClaptrapViewModel

class LoginViewModel : ClaptrapViewModel<
  LoginViewState,
  LoginViewEvent,
  LoginViewEffect
>(LoginViewState) {
  override fun processInput(viewEvent: LoginViewEvent) {
    if (viewEvent is LoginViewEvent.SignInWithGoogle) {
      signInWithGoogle()
    }
  }

  private fun signInWithGoogle() = sideEffect {
    viewEffects.emit(LoginViewEffect.SignInWithGoogle)
  }
}
