package com.aradipatrik.claptrap.backdrop.ui

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.transition.TransitionManager
import com.aradipatrik.claptrap.R
import com.aradipatrik.claptrap.backdrop.model.*
import com.aradipatrik.claptrap.backdrop.model.BackdropViewEvent.SelectTopLevelScreen
import com.aradipatrik.claptrap.common.backdrop.Backdrop
import com.aradipatrik.claptrap.databinding.FragmentMainBinding
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.mvi.MviUtil.ignore
import com.aradipatrik.claptrap.theme.widget.MotionUtil.playTransitionAndWaitForFinish
import com.aradipatrik.claptrap.theme.widget.ViewUtil.visibleInMotionLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

@AndroidEntryPoint
class BackdropFragment : ClapTrapFragment<
  BackdropViewState,
  BackdropViewEvent,
  BackdropViewEffect,
  FragmentMainBinding
  >(R.layout.fragment_main, FragmentMainBinding::inflate), Backdrop {
  override val viewModel by activityViewModels<BackdropViewModel>()

  override val viewEvents get() = merge(
    binding.transactionsMenuItem.clicks.map { SelectTopLevelScreen(TopLevelScreen.TRANSACTION_HISTORY) },
    binding.walletsMenuItem.clicks.map { SelectTopLevelScreen(TopLevelScreen.WALLETS) },
    binding.statisticsMenuItem.clicks.map { SelectTopLevelScreen(TopLevelScreen.STATISTICS) }
  )

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initNavigation()
    savedInstanceState?.getBundle(MOTION_LAYOUT_STATE_KEY)
      ?.let(binding.backdropMotionLayout::setTransitionState)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putBundle(MOTION_LAYOUT_STATE_KEY, binding.backdropMotionLayout.transitionState)
  }

  private fun initNavigation() {
    val nestedNavHostFragment = childFragmentManager
      .findFragmentById(R.id.child_host) as NavHostFragment
    val nestedNavController = nestedNavHostFragment.navController

    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
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
    is BackdropViewState.CustomMenuShowing -> showCustomMenu(viewState.menuFragment)
  }

  private fun showCustomMenu(menuFragment: Fragment) {
    childFragmentManager.commit {
      setReorderingAllowed(true)
      replace(R.id.custom_menu_container, menuFragment)
    }

    lifecycleScope.launchWhenResumed {
      binding.backdropMotionLayout
        .playTransitionAndWaitForFinish(R.id.toolbar_shown, R.id.toolbar_hidden)
    }
  }

  private fun activateScreen(topLevelScreen: TopLevelScreen) {
    activateMenu(topLevelScreen)
    setTitle(topLevelScreen)
  }

  private fun setTitle(topLevelScreen: TopLevelScreen) = when (topLevelScreen) {
    TopLevelScreen.TRANSACTION_HISTORY -> binding.title.text = getString(R.string.transaction_history)
    TopLevelScreen.WALLETS -> binding.title.text = getString(R.string.wallets)
    TopLevelScreen.STATISTICS -> binding.title.text = getString(R.string.statistics)
  }

  private fun activateMenu(topLevelScreen: TopLevelScreen) = when (topLevelScreen) {
    TopLevelScreen.TRANSACTION_HISTORY -> activateMenuItem(binding.transactionsMenuItem)
    TopLevelScreen.WALLETS -> activateMenuItem(binding.walletsMenuItem)
    TopLevelScreen.STATISTICS -> activateMenuItem(binding.statisticsMenuItem)
  }

  private fun activateMenuItem(menuItem: BackdropBackLayerMenuItemView) {
    binding.transactionsMenuItem.deactivate()
    binding.walletsMenuItem.deactivate()
    binding.statisticsMenuItem.deactivate()
    menuItem.activate()
  }

  override fun react(viewEffect: BackdropViewEffect) = when(viewEffect) {
    BackdropViewEffect.RevealBackLayer -> binding.menuIcon.performClick().ignore()
    BackdropViewEffect.ConcealBackLayer -> binding.menuIcon.performClick().ignore()
  }

  override fun switchMenu(menuFragment: Fragment) =
    viewModel.processInput(BackdropViewEvent.SwitchToCustomMenu(menuFragment))

  override fun clearMenu() = viewModel.processInput(BackdropViewEvent.RemoveCustomMenu)

  companion object {
    private const val MOTION_LAYOUT_STATE_KEY = "MOTION_LAYOUT_STATE_KEY"
  }
}
