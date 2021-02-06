package com.aradipatrik.claptrap.apimodels

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserWire(
  val id: String,
  val email: String?,
  val name: String?,
  val profilePictureUrl: String?,
)
