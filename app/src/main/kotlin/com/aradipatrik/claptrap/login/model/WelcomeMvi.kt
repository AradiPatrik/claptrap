package com.aradipatrik.claptrap.login.model

import com.aradipatrik.claptrap.domain.User

sealed class WelcomeBackViewEffect {
  object ShowSignInWithGoogleOAuthFlow : WelcomeBackViewEffect()
  object NavigateToMainScreen : WelcomeBackViewEffect()
}

sealed class WelcomeBackViewEvent {
  object SignInWithGoogle : WelcomeBackViewEvent()
  data class SignInSuccessful(val user: User) : WelcomeBackViewEvent()
}

object WelcomeBackViewState
