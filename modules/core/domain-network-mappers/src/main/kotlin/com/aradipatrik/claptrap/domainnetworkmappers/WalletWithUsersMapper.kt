package com.aradipatrik.claptrap.domainnetworkmappers

import com.aradipatrik.claptrap.apimodels.WalletWithUsersWire
import com.aradipatrik.claptrap.domain.WalletWithUsers
import com.aradipatrik.claptrap.domainnetworkmappers.WalletMapper.toWire

object WalletWithUsersMapper {
  @JvmName("toWireInstance")
  fun WalletWithUsers.toWire() = WalletWithUsersWire(
    wallet = wallet.toWire(),
    users = users.map(UserMapper::toWire)
  )

  fun toWire(walletWithUsers: WalletWithUsers) = walletWithUsers.toWire()
}
