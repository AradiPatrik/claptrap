package com.aradipatrik.claptrap.theme.widget

import android.os.Bundle
import android.os.Handler
import android.view.ViewTreeObserver
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.os.bundleOf
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object MotionUtil {
  suspend fun MotionLayout.awaitStateReached(stateId: Int) {
    suspendCancellableCoroutine<Unit> { continuation ->
      val listener = object : TransitionAdapter() {
        override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
          if (currentId == stateId) {
            removeTransitionListener(this)
            continuation.resume(Unit)
          }
        }
      }

      continuation.invokeOnCancellation {
        removeTransitionListener(listener)
      }

      addTransitionListener(listener)
    }
  }

  suspend fun MotionLayout.playTransitionAndWaitForFinish(beginState: Int, endState: Int) {
    setTransition(beginState, endState)
    transitionToState(endState)
    awaitStateReached(endState)
  }

  suspend fun MotionLayout.playReverseTransitionAndWaitForFinish(beginState: Int, endState: Int) {
    setTransition(beginState, endState)
    progress = 1f
    transitionToState(beginState)
    awaitStateReached(beginState)
  }

  fun MotionLayout.playReverseTransition(beginState: Int, endState: Int) {
    setTransition(beginState, endState)
    progress = 1f
    transitionToState(beginState)
  }

  fun MotionLayout.playTransition(beginState: Int, endState: Int) {
    setTransition(beginState, endState)
    progress = 0.0f
    transitionToState(endState)
  }

  fun MotionLayout.restoreState(savedInstanceState: Bundle?, key: String) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
      override fun onGlobalLayout() {
        viewTreeObserver.removeOnGlobalLayoutListener(this)
        doRestore(savedInstanceState, key)
      }
    })
  }

  private fun MotionLayout.doRestore(savedInstanceState: Bundle?, key: String) =
    savedInstanceState?.let {
      val motionBundle = savedInstanceState.getBundle(key) ?: error("$key state was not saved")
      setTransition(
        motionBundle.getInt("claptrap.motion.startState", -1)
          .takeIf { it != -1 }
          ?: error("Could not retrieve start state for $key"),
        motionBundle.getInt("claptrap.motion.endState", -1)
          .takeIf { it != -1 }
          ?: error("Could not retrieve end state for $key")
      )
      progress = motionBundle.getFloat("claptrap.motion.progress", -1.0f)
        .takeIf { it != -1.0f }
        ?: error("Could not retrieve progress for $key")
    }

  fun MotionLayout.saveState(outState: Bundle, key: String) {
    outState.putBundle(
      key,
      bundleOf(
        "claptrap.motion.startState" to startState,
        "claptrap.motion.endState" to endState,
        "claptrap.motion.progress" to progress
      )
    )
  }
}
