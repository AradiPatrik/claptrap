package com.aradipatrik.claptrap.backdrop.model

import com.aradipatrik.claptrap.backdrop.model.BackdropViewEvent.SelectTopLevelScreen
import com.aradipatrik.claptrap.backdrop.model.BackdropViewState.OnTopLevelScreen
import com.aradipatrik.claptrap.backdrop.model.TopLevelScreen.TRANSACTION_HISTORY
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel
import kotlinx.coroutines.delay

class BackdropViewModel : ClaptrapViewModel<BackdropViewState, BackdropViewEvent, BackdropViewEffect>(
  OnTopLevelScreen(TRANSACTION_HISTORY)
) {
  override fun processInput(viewEvent: BackdropViewEvent) = when(viewEvent) {
    is SelectTopLevelScreen -> setState<OnTopLevelScreen> { state ->
      viewEffects.send(BackdropViewEffect.ConcealBackLayer)
      state.copy(topLevelScreen = viewEvent.topLevelScreen)
    }
  }
}