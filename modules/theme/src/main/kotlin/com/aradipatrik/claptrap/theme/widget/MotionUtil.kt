package com.aradipatrik.claptrap.theme.widget

import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
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
}
