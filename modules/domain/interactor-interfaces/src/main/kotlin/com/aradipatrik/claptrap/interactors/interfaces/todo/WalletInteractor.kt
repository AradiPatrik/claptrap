package com.aradipatrik.claptrap.interactors.interfaces.todo

import com.aradipatrik.claptrap.domain.Wallet

interface WalletInteractor {
  suspend fun getAllWallets(): List<Wallet>
}
