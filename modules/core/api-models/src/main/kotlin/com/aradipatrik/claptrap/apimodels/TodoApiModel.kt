package com.aradipatrik.claptrap.apimodels

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TodoApiModel(
  val id: String,
  val name: String,
  val isDone: Boolean
)
