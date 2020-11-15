package com.aradipatrik.claptrap.backdrop.model

sealed class BackdropViewEvent {
  class SelectTopLevelScreen(val topLevelScreen: TopLevelScreen) : BackdropViewEvent()
}