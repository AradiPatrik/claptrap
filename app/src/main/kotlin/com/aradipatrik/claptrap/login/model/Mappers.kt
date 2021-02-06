package com.aradipatrik.claptrap.login.model

import com.aradipatrik.claptrap.domain.User
import com.google.android.gms.auth.api.identity.SignInCredential

object Mappers {
  fun User.Companion.fromGoogleCredentials(signInCredential: SignInCredential) = User(
    idToken = signInCredential.googleIdToken!!,
    id = signInCredential.id,
    name = signInCredential.displayName,
    profilePictureUri = signInCredential.profilePictureUri?.toString(),
    email = null
  )
}
