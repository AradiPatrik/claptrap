package com.aradipatrik.claptrap.domain

import org.joda.money.Money

data class Wallet(
  val isPrivate: Boolean,
  val moneyInWallet: Money,
  val name: String
)
