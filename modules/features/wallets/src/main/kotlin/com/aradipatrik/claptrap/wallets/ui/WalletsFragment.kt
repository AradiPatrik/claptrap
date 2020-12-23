package com.aradipatrik.claptrap.wallets.ui

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.aradipatrik.claptrap.common.backdrop.backdrop
import com.aradipatrik.claptrap.feature.wallets.R
import com.aradipatrik.claptrap.feature.wallets.databinding.FragmentWalletsBinding
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.theme.widget.ViewUtils.modify
import com.aradipatrik.claptrap.wallets.model.WalletsViewEffect
import com.aradipatrik.claptrap.wallets.model.WalletsViewEvent
import com.aradipatrik.claptrap.wallets.model.WalletsViewModel
import com.aradipatrik.claptrap.wallets.model.WalletsViewState
import com.aradipatrik.claptrap.wallets.model.WalletsViewState.Loading
import com.aradipatrik.claptrap.wallets.model.WalletsViewState.WalletsLoaded
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import org.joda.money.format.MoneyFormatter
import org.joda.money.format.MoneyFormatterBuilder
import ru.ldralighieri.corbind.view.clicks
import javax.inject.Inject

@AndroidEntryPoint
class WalletsFragment : ClapTrapFragment<
  WalletsViewState,
  WalletsViewEvent,
  WalletsViewEffect,
  FragmentWalletsBinding>(R.layout.fragment_wallets, FragmentWalletsBinding::inflate) {
  override val viewModel by viewModels<WalletsViewModel>()
  override val viewEvents get() = emptyFlow<WalletsViewEvent>()



  override fun render(viewState: WalletsViewState) = when(viewState) {
    Loading -> { }
    is WalletsLoaded -> renderLoaded(viewState)
  }

  private fun renderLoaded(viewState: WalletsLoaded) {
    binding.total.text = MoneyFormatterBuilder()
      .appendCurrencySymbolLocalized()
      .appendLiteral(" ")
      .appendCurrencySymbolLocalized()
      .toFormatter()
      .print(viewState.total)
  }

  override fun react(viewEffect: WalletsViewEffect) {

  }
}
