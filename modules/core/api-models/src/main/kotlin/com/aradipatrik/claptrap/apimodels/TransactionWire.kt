package com.aradipatrik.claptrap.apimodels

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionWire(
  val id: String? = null,
  val memo: String
)
