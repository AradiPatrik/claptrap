package com.aradipatrik.claptrap.login.model

import com.aradipatrik.claptrap.domain.User

sealed class WelcomeBackViewEffect {
  object ShowSignInWithGoogleOAuthFlow : WelcomeBackViewEffect()
  object NavigateToMainScreen : WelcomeBackViewEffect()
}

sealed class WelcomeBackViewEvent {
  object SignInWithGoogle : WelcomeBackViewEvent()
  object SignInWithEmailAndPassword : WelcomeBackViewEvent()
  data class EmailTextChange(val email: String) : WelcomeBackViewEvent()
  data class PasswordTextChange(val password: String) : WelcomeBackViewEvent()
  data class SignInSuccessful(val user: User) : WelcomeBackViewEvent()
}

data class WelcomeBackViewState(
  val email: String = "",
  val password: String = ""
)
