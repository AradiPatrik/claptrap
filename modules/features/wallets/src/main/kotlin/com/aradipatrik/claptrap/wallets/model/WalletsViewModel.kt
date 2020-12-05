package com.aradipatrik.claptrap.wallets.model

import androidx.hilt.lifecycle.ViewModelInject
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel

class WalletsViewModel @ViewModelInject constructor()
  : ClaptrapViewModel<WalletsViewState, WalletsViewEvent, WalletsViewEffect>(
  WalletsViewState.Placeholder
) {
  override fun processInput(viewEvent: WalletsViewEvent) {
  }
}