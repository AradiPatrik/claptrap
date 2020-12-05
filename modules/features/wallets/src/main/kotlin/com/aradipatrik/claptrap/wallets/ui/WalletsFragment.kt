package com.aradipatrik.claptrap.wallets.ui

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.aradipatrik.claptrap.feature.wallets.R
import com.aradipatrik.claptrap.feature.wallets.databinding.FragmentWalletsBinding
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.wallets.model.WalletsViewEffect
import com.aradipatrik.claptrap.wallets.model.WalletsViewEvent
import com.aradipatrik.claptrap.wallets.model.WalletsViewModel
import com.aradipatrik.claptrap.wallets.model.WalletsViewState
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.emptyFlow

@AndroidEntryPoint
class WalletsFragment : ClapTrapFragment<
  WalletsViewState,
  WalletsViewEvent,
  WalletsViewEffect,
  FragmentWalletsBinding>(R.layout.fragment_wallets, FragmentWalletsBinding::inflate) {
  override val viewModel by viewModels<WalletsViewModel>()
  override val viewEvents = emptyFlow<WalletsViewEvent>()

  override fun render(viewState: WalletsViewState) {
  }

  override fun react(viewEffect: WalletsViewEffect) {
  }
}
