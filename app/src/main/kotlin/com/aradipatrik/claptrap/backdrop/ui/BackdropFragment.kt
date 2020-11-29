package com.aradipatrik.claptrap.backdrop.ui

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aradipatrik.claptrap.R
import com.aradipatrik.claptrap.backdrop.model.*
import com.aradipatrik.claptrap.backdrop.model.BackdropViewEvent.SelectTopLevelScreen
import com.aradipatrik.claptrap.common.backdrop.BackEffect
import com.aradipatrik.claptrap.common.backdrop.BackListener
import com.aradipatrik.claptrap.common.backdrop.Backdrop
import com.aradipatrik.claptrap.databinding.FragmentMainBinding
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.mvi.MviUtil.ignore
import com.aradipatrik.claptrap.theme.widget.MotionUtil.playTransition
import com.aradipatrik.claptrap.theme.widget.MotionUtil.restoreState
import com.aradipatrik.claptrap.theme.widget.MotionUtil.saveState
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
    binding.backdropMotionLayout.restoreState(savedInstanceState, MOTION_LAYOUT_STATE_KEY)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    binding.backdropMotionLayout.saveState(outState, MOTION_LAYOUT_STATE_KEY)
  }

  private fun initNavigation() {
    val nestedNavHostFragment = childFragmentManager
      .findFragmentById(R.id.child_host) as NavHostFragment
    val nestedNavController = nestedNavHostFragment.navController

    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
      if (binding.backdropMotionLayout.isOpen) {
        lifecycleScope.launchWhenResumed {
          viewModel.viewEffects.send(BackdropViewEffect.ConcealBackLayer)
        }
      } else {
        notifyChildrenOfBackEventAndPopIfNecessary(nestedNavHostFragment, nestedNavController)
      }
    }
  }

  private fun OnBackPressedCallback.notifyChildrenOfBackEventAndPopIfNecessary(
    nestedNavHostFragment: NavHostFragment,
    nestedNavController: NavController
  ) {
    val backEffects = nestedNavHostFragment.childFragmentManager.fragments
      .plus(childFragmentManager.fragments)
      .filterIsInstance<BackListener>()
      .map { it.onBack() }

    if (backEffects.any { it == BackEffect.POP } || backEffects.isEmpty()) {
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
      replace(R.id.custom_menu_container, menuFragment, MENU_FRAGMENT_TAG)
    }

    binding.backdropMotionLayout.playTransition(R.id.toolbar_shown, R.id.toolbar_hidden)
  }

  private fun hideCustomMenu() = childFragmentManager.findFragmentByTag(MENU_FRAGMENT_TAG)?.let {
    childFragmentManager.commit {
      setReorderingAllowed(true)
      remove(it)
    }

    binding.backdropMotionLayout.playTransition(R.id.toolbar_hidden, R.id.toolbar_shown)
  }

  private fun activateScreen(topLevelScreen: TopLevelScreen) {
    hideCustomMenu()
    activateDestination(topLevelScreen)
    setTitle(topLevelScreen)
  }

  private fun setTitle(topLevelScreen: TopLevelScreen) = when (topLevelScreen) {
    TopLevelScreen.TRANSACTION_HISTORY -> binding.title.text = getString(R.string.transaction_history)
    TopLevelScreen.WALLETS -> binding.title.text = getString(R.string.wallets)
    TopLevelScreen.STATISTICS -> binding.title.text = getString(R.string.statistics)
  }

  private fun activateDestination(topLevelScreen: TopLevelScreen) = when (topLevelScreen) {
    TopLevelScreen.TRANSACTION_HISTORY -> activateDestinationItem(binding.transactionsMenuItem)
    TopLevelScreen.WALLETS -> activateDestinationItem(binding.walletsMenuItem)
    TopLevelScreen.STATISTICS -> activateDestinationItem(binding.statisticsMenuItem)
  }

  private fun activateDestinationItem(menuItem: BackdropBackLayerMenuItemView) {
    binding.transactionsMenuItem.deactivate()
    binding.walletsMenuItem.deactivate()
    binding.statisticsMenuItem.deactivate()
    menuItem.activate()
  }

  override fun react(viewEffect: BackdropViewEffect) = when (viewEffect) {
    BackdropViewEffect.RevealBackLayer -> binding.menuIcon.performClick().ignore()
    BackdropViewEffect.ConcealBackLayer -> binding.menuIcon.performClick().ignore()
    BackdropViewEffect.MorphFromBackToMenu -> lifecycleScope.launchWhenResumed {
      val oldMenuIcon = binding.menuIcon.startToEndAnimatedVectorDrawable
      binding.menuIcon.startToEndAnimatedVectorDrawable = ContextCompat
        .getDrawable(requireContext(), R.drawable.arrow_to_menu) as AnimatedVectorDrawable
      binding.menuIcon.morphAndWait()
      binding.menuIcon.startToEndAnimatedVectorDrawable = oldMenuIcon
    }.ignore()
  }

  override fun switchMenu(menuFragment: Fragment) =
    viewModel.processInput(BackdropViewEvent.SwitchToCustomMenu(menuFragment))

  override fun clearMenu() {
    viewModel.processInput(BackdropViewEvent.RemoveCustomMenu)
  }

  companion object {
    private const val MOTION_LAYOUT_STATE_KEY = "MOTION_LAYOUT_STATE_KEY"
    private const val MENU_FRAGMENT_TAG = "MENU_FRAGMENT_TAG"
  }

  private val MotionLayout.isOpen get() = currentState == R.id.menu_shown
}

