package com.aradipatrik.claptrap.backdrop.model

import com.aradipatrik.claptrap.backdrop.model.BackdropViewEffect.*
import com.aradipatrik.claptrap.backdrop.model.BackdropViewEvent.*
import com.aradipatrik.claptrap.backdrop.model.BackdropViewState.OnTopLevelScreen
import com.aradipatrik.claptrap.backdrop.model.TopLevelScreen.TRANSACTION_HISTORY
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEffect
import com.aradipatrik.claptrap.mvi.ClaptrapViewModel
import timber.log.Timber

class BackdropViewModel : ClaptrapViewModel<BackdropViewState, BackdropViewEvent, BackdropViewEffect>(
  OnTopLevelScreen(TRANSACTION_HISTORY, isBackLayerConcealed = true)
) {
  override fun processInput(viewEvent: BackdropViewEvent) = when (viewEvent) {
    is SelectTopLevelScreen -> reduceSpecificState<OnTopLevelScreen> { oldState ->
      if (oldState.topLevelScreen != viewEvent.topLevelScreen) {
        viewEffects.send(NavigateToDestination(viewEvent.topLevelScreen))
      }
      viewEffects.send(ConcealBackLayer)
      oldState.copy(topLevelScreen = viewEvent.topLevelScreen, isBackLayerConcealed = true)
    }
    is SwitchToCustomMenu -> reduceState { oldState ->
      BackdropViewState.CustomMenuShowing(viewEvent.menuFragment, oldState.topLevelScreen)
    }
    is RemoveCustomMenu -> reduceState { oldState ->
      viewEffects.send(MorphFromBackToMenu)
      OnTopLevelScreen(oldState.topLevelScreen, isBackLayerConcealed = true)
    }
    BackdropConcealToggle -> reduceSpecificState<OnTopLevelScreen> { state ->
      if (state.isBackLayerConcealed) {
        viewEffects.send(RevealBackLayer)
        state.copy(isBackLayerConcealed = false)
      } else {
        viewEffects.send(ConcealBackLayer)
        state.copy(isBackLayerConcealed = true)
      }
    }
  }
}
