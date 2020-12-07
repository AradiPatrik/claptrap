package com.aradipatrik.claptrap.backdrop.model

import androidx.fragment.app.Fragment

sealed class BackdropViewEvent {
  class SelectTopLevelScreen(val topLevelScreen: TopLevelScreen) : BackdropViewEvent()
  data class SwitchToCustomMenu(val menuFragmentClass: Class<out Fragment>): BackdropViewEvent()
  object RemoveCustomMenu : BackdropViewEvent()
  object BackdropConcealToggle : BackdropViewEvent()
}
