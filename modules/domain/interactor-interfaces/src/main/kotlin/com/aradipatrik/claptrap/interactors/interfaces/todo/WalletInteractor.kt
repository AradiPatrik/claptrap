package com.aradipatrik.claptrap.interactors.interfaces.todo

import com.aradipatrik.claptrap.domain.Wallet
import kotlinx.coroutines.flow.Flow

interface WalletInteractor {
  fun getAllWalletsFlow(): Flow<List<Wallet>>
  fun getSelectedWalletIdFlow(): Flow<String>

  suspend fun getAllWallets(): List<Wallet>
  suspend fun getSelectedWalletId(): String
  suspend fun setSelectedWalletId(id: String)
}
