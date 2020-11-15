package com.aradipatrik.claptrap.backdrop.ui

import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.aradipatrik.claptrap.R
import com.aradipatrik.claptrap.backdrop.model.*
import com.aradipatrik.claptrap.backdrop.model.BackdropViewEvent.SelectTopLevelScreen
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.mvi.MviUtil.ignore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main_navigation_drawer.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

@AndroidEntryPoint
class BackdropFragment : ClapTrapFragment<BackdropViewState, BackdropViewEvent, BackdropViewEffect>(R.layout.fragment_main) {
  override val viewModel by viewModels<BackdropViewModel>()

  override val viewEvents get() = merge(
    transactions_menu_item.clicks.map { SelectTopLevelScreen(TopLevelScreen.TRANSACTION_HISTORY) },
    wallets_menu_item.clicks.map { SelectTopLevelScreen(TopLevelScreen.WALLETS) },
    statistics_menu_item.clicks.map { SelectTopLevelScreen(TopLevelScreen.STATISTICS) }
  )

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initNavigation()
  }

  private fun initNavigation() {
    val nestedNavHostFragment = childFragmentManager
      .findFragmentById(R.id.child_host) as NavHostFragment
    val nestedNavController = nestedNavHostFragment.navController

    requireActivity().onBackPressedDispatcher.addCallback(this) {
      if (nestedNavController.previousBackStackEntry != null) {
        nestedNavController.popBackStack()
      } else {
        isEnabled = false
        requireActivity().onBackPressed()
        isEnabled = true
      }
    }
  }

  override fun render(viewState: BackdropViewState) = when(viewState) {
    is BackdropViewState.OnTopLevelScreen -> activateScreen(viewState.topLevelScreen)
  }

  private fun activateScreen(topLevelScreen: TopLevelScreen) {
    activateMenu(topLevelScreen)
    setTitle(topLevelScreen)
  }

  private fun setTitle(topLevelScreen: TopLevelScreen) = when (topLevelScreen) {
    TopLevelScreen.TRANSACTION_HISTORY -> title.text = getString(R.string.transaction_history)
    TopLevelScreen.WALLETS -> title.text = getString(R.string.wallets)
    TopLevelScreen.STATISTICS -> title.text = getString(R.string.statistics)
  }

  private fun activateMenu(topLevelScreen: TopLevelScreen) = when (topLevelScreen) {
    TopLevelScreen.TRANSACTION_HISTORY -> activateMenuItem(transactions_menu_item)
    TopLevelScreen.WALLETS -> activateMenuItem(wallets_menu_item)
    TopLevelScreen.STATISTICS -> activateMenuItem(statistics_menu_item)
  }

  private fun activateMenuItem(menuItem: BackdropBackLayerMenuItemView) {
    transactions_menu_item.deactivate()
    wallets_menu_item.deactivate()
    statistics_menu_item.deactivate()
    menuItem.activate()
  }

  override fun react(viewEffect: BackdropViewEffect) = when(viewEffect) {
    BackdropViewEffect.RevealBackLayer -> menu_icon.performClick().ignore()
    BackdropViewEffect.ConcealBackLayer -> menu_icon.performClick().ignore()
  }
}
