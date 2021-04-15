package com.aradipatrik.claptrap.domainnetworkmappers

import com.aradipatrik.claptrap.apimodels.WalletWire
import com.aradipatrik.claptrap.domain.Wallet

object WalletMapper {
  @JvmName("toWireInstance")
  fun Wallet.toWire() = WalletWire(
    id = id,
    name = name,
    moneyInWallet = moneyInWallet
  )

  fun toWire(wallet: Wallet) = wallet.toWire()

}
