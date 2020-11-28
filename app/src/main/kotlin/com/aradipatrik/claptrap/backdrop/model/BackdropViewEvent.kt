package com.aradipatrik.claptrap.backdrop.model

import androidx.fragment.app.Fragment

sealed class BackdropViewEvent {
  class SelectTopLevelScreen(val topLevelScreen: TopLevelScreen) : BackdropViewEvent()
  data class SwitchToCustomMenu(val menuFragment: Fragment): BackdropViewEvent()
  object RemoveCustomMenu : BackdropViewEvent()
}
