package com.aradipatrik.claptrap.interactors

import com.aradipatrik.claptrap.domain.Wallet
import com.aradipatrik.claptrap.interactors.interfaces.todo.WalletInteractor
import kotlinx.coroutines.flow.Flow

class WalletInteractorImpl : WalletInteractor {
  override fun getAllWalletsFlow(): Flow<List<Wallet>> {
    TODO("Not yet implemented")
  }

  override fun getSelectedWalletIdFlow(): Flow<String> {
    TODO("Not yet implemented")
  }

  override suspend fun getAllWallets(): List<Wallet> {
    TODO("Not yet implemented")
  }

  override suspend fun getSelectedWalletId(): String {
    TODO("Not yet implemented")
  }

  override suspend fun setSelectedWalletId(id: String) {
    TODO("Not yet implemented")
  }
}