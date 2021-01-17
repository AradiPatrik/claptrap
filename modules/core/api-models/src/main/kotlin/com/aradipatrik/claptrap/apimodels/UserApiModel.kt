package com.aradipatrik.claptrap.apimodels

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserApiModel(
  val id: String,
  val email: String,
)
