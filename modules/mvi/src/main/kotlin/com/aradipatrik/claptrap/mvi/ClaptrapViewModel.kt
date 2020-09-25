package com.aradipatrik.claptrap.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

typealias StateReducer<T, V> = suspend (T) -> V
typealias SideEffect<T> = suspend (T) -> Unit

abstract class ClaptrapViewModel<VS, VE>(initialState: VS) : ViewModel() {
  val reducerChannel = Channel<StateReducer<VS, VS>>(BUFFERED)

  private val _viewState = MutableStateFlow(initialState)
  val viewState: StateFlow<VS> = _viewState

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


  protected inline fun <reified T: VS> setState(noinline stateReducer: StateReducer<T, VS>) {
    viewModelScope.launch {
      reducerChannel.send { state ->
        require(state is T)
        stateReducer.invoke(state)
      }
    }
  }

  protected inline fun <reified T: VS> withState(noinline sideEffect: SideEffect<T>) {
    viewModelScope.launch {
      reducerChannel.send { state ->
        require(state is T)
        state.also { sideEffect(state) }
      }
    }
  }

  abstract fun processInput(viewEvent: VE)
}
