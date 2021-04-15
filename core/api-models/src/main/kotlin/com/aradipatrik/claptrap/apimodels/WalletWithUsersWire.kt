package com.aradipatrik.claptrap.apimodels

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WalletWithUsersWire(
  val wallet: WalletWire,
  val users: List<UserWire>,
)
