package com.aradipatrik.claptrap.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.concurrent.timerTask
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

typealias StateReducer<T, V> = suspend (T) -> V
typealias SideEffect<T> = suspend (T) -> Unit
typealias StateSetter<T> = suspend () -> T

abstract class ClaptrapViewModel<S, EV, EF>(initialState: S) : ViewModel() {
  val reducerChannel = Channel<StateReducer<S, S>>(BUFFERED)
  val viewEffects = BroadcastChannel<EF>(BUFFERED)

  private val _viewState = MutableStateFlow(initialState)
  val viewState: StateFlow<S> = _viewState

  init {
    reducerChannel.receiveAsFlow()
      .scan(initialState) { state, reducer ->
        reducer.invoke(state)
      }
      .distinctUntilChanged()
      .onEach {
        Timber.tag(this::class.simpleName).d(it.toString())
        _viewState.value = it
      }
      .launchIn(viewModelScope)
  }

  protected fun reduceState(stateReducer: StateReducer<S, S>) {
    viewModelScope.launch {
      reducerChannel.send {
        stateReducer.invoke(it)
      }
    }
  }

  protected inline fun <reified T: S> reduceSpecificState(noinline stateReducer: StateReducer<T, S>) {
    viewModelScope.launch {
      reducerChannel.send { state ->
        require(state is T)
        stateReducer.invoke(state)
      }
    }
  }

  protected inline fun <reified T: S> withState(noinline sideEffect: SideEffect<T>) {
    viewModelScope.launch {
      reducerChannel.send { state ->
        require(state is T)
        state.also { sideEffect(state) }
      }
    }
  }

  protected fun sideEffect(sideEffect: SideEffect<S>) {
    viewModelScope.launch {
      reducerChannel.send { state ->
        state.also { sideEffect(state) }
      }
    }
  }

  abstract fun processInput(viewEvent: EV)
}
