package com.aradipatrik.claptrap.backdrop.model

import androidx.fragment.app.Fragment

sealed class BackdropViewState {
  abstract val topLevelScreen: TopLevelScreen

  data class OnTopLevelScreen(
    override val topLevelScreen: TopLevelScreen,
    val isBackLayerConcealed: Boolean
  ): BackdropViewState()
  data class CustomMenuShowing(
    val menuFragment: Fragment,
    override val topLevelScreen: TopLevelScreen
  ) : BackdropViewState()
}
