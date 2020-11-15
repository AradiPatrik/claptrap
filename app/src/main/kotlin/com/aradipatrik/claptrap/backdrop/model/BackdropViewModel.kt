package com.aradipatrik.claptrap.backdrop.model

import com.aradipatrik.claptrap.backdrop.model.BackdropViewEvent.SelectTopLevelScreen
import com.aradipatrik.claptrap.backdrop.model.BackdropViewState.OnTopLevelScreen
import com.aradipatrik.claptrap.backdrop.model.TopLevelScreen.TRANSACTION_HISTORY
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel

class BackdropViewModel : ClaptrapViewModel<BackdropViewState, BackdropViewEvent, BackdropViewEffect>(
  OnTopLevelScreen(TRANSACTION_HISTORY)
) {
  override fun processInput(viewEvent: BackdropViewEvent) = when(viewEvent) {
    is SelectTopLevelScreen -> reduceState<OnTopLevelScreen> { oldState ->
      viewEffects.send(BackdropViewEffect.ConcealBackLayer)
      oldState.copy(topLevelScreen = viewEvent.topLevelScreen)
    }
  }
}