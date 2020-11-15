package com.aradipatrik.claptrap.backdrop.model

sealed class BackdropViewState {
  data class OnTopLevelScreen(val topLevelScreen: TopLevelScreen): BackdropViewState()
}
