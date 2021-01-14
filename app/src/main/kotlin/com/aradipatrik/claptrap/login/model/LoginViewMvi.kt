package com.aradipatrik.claptrap.login.model

sealed class LoginViewEffect {
  object SignInWithGoogle : LoginViewEffect()
}

sealed class LoginViewEvent {
  object SignInWithGoogle : LoginViewEvent()
}

object LoginViewState
