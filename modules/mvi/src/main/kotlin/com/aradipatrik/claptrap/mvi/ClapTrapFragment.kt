package com.aradipatrik.claptrap.mvi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.aradipatrik.claptrap.mvi.Flows.launchInWhenResumed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

typealias InflaterFunction<B> = (LayoutInflater, ViewGroup?, Boolean) -> B

abstract class ClapTrapFragment<VS, EV, EF, B: ViewBinding>(
  private val layout: Int,
  private val inflaterFunction: InflaterFunction<B>
) : Fragment() {
  private var _binding: B? = null

  abstract val viewModel: ClaptrapViewModel<VS, EV, EF>

  abstract val viewEvents: Flow<EV>

  abstract fun render(viewState: VS)

  abstract fun react(viewEffect: EF)

  open fun initViews(savedInstanceState: Bundle?) { }

  protected val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = inflaterFunction.invoke(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initViews(savedInstanceState)

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

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
