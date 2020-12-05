package com.aradipatrik.claptrap.backdrop.model

sealed class BackdropViewEffect {
  object RevealBackLayer : BackdropViewEffect()
  object ConcealBackLayer : BackdropViewEffect()
  object MorphFromBackToMenu : BackdropViewEffect()

  data class NavigateToDestination(val destination: TopLevelScreen) : BackdropViewEffect()
}
