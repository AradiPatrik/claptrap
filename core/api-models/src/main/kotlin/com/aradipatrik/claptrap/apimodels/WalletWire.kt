package com.aradipatrik.claptrap.apimodels

import org.joda.money.Money
import java.util.UUID

data class WalletWire(
  val id: UUID,
  val name: String,
  val moneyInWallet: Money,
)
