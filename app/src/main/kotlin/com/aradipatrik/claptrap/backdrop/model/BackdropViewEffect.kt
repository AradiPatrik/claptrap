package com.aradipatrik.claptrap.backdrop.model

import androidx.fragment.app.Fragment

sealed class BackdropViewEffect {
  object MorphFromBackToMenu : BackdropViewEffect()

  data class ShowCustomMenu(val menuFragment: Class<out Fragment>) : BackdropViewEffect()
  data class NavigateToDestination(val destination: TopLevelScreen) : BackdropViewEffect()
}
