package com.aradipatrik.claptrap.fakeinteractors.wallet

import com.aradipatrik.claptrap.domain.Wallet
import com.aradipatrik.claptrap.fakeinteractors.generators.CommonMockGenerator.of
import com.aradipatrik.claptrap.fakeinteractors.generators.WalletMockGenerator.nextWallet
import com.aradipatrik.claptrap.interactors.interfaces.todo.WalletInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import javax.inject.Inject
import kotlin.random.Random

class WalletInteractorFake @Inject constructor() : WalletInteractor {
  private val wallets = MutableStateFlow(3 of { Random.nextWallet() })

  override suspend fun getAllWallets(): List<Wallet> = wallets.take(1).first()
}