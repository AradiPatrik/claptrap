package com.aradipatrik.claptrap.domain

data class User(
  val idToken: String,
  val id: String,
  val name: String?,
  val profilePictureUri: String?
) {
  companion object
}
