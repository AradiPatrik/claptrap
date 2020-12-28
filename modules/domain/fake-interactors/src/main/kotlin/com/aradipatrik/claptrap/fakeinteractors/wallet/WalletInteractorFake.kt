package com.aradipatrik.claptrap.fakeinteractors.wallet

import com.aradipatrik.claptrap.domain.Wallet
import com.aradipatrik.claptrap.fakeinteractors.generators.CommonMockGenerator.of
import com.aradipatrik.claptrap.fakeinteractors.generators.WalletMockGenerator.nextWallet
import com.aradipatrik.claptrap.interactors.interfaces.todo.WalletInteractor
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class WalletInteractorFake @Inject constructor() : WalletInteractor {
  private val wallets = MutableStateFlow(3 of { Random.nextWallet() })
  private val selectedWalletId = MutableStateFlow("")

  override fun getAllWalletsFlow(): Flow<List<Wallet>> = wallets

  override fun getSelectedWalletIdFlow(): Flow<String> = selectedWalletId
    .map {
      if (it.isEmpty()) {
        getAllWallets().first().id
      } else {
        it
      }
    }

  override suspend fun getAllWallets(): List<Wallet> = wallets.first()

  override suspend fun getSelectedWalletId() = selectedWalletId.first()

  override suspend fun setSelectedWalletId(id: String) {
    selectedWalletId.value = id
  }
}