package com.aradipatrik.claptrap.mvi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.aradipatrik.claptrap.mvi.Flows.launchInWhenResumed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

abstract class ClapTrapFragment<VS, EV, EF>(layout: Int) : Fragment(layout) {
  abstract val viewModel: ClaptrapViewModel<VS, EV, EF>

  abstract val viewEvents: Flow<EV>

  abstract fun render(viewState: VS)

  abstract fun react(viewEffect: EF)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.viewState
      .onEach(::render)
      .launchInWhenResumed(lifecycleScope)

    viewEvents
      .onEach(viewModel::processInput)
      .launchInWhenResumed(lifecycleScope)

    viewModel.viewEffects.receiveAsFlow()
      .onEach(::react)
      .launchInWhenResumed(lifecycleScope)
  }
}
