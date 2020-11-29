package com.aradipatrik.claptrap.theme.widget

import android.content.Context
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatImageButton
import com.aradipatrik.claptrap.theme.R
import com.aradipatrik.claptrap.theme.widget.ViewUtil.withStyleable
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


class AnimatedVectorDrawableImageButton@JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  @AttrRes defStyleAttr: Int = 0
) : AppCompatImageButton(context, attrs, defStyleAttr) {
  private var isAtStartState = true

  lateinit var startToEndAnimatedVectorDrawable: AnimatedVectorDrawable
  lateinit var endToStartAnimatedVectorDrawable: AnimatedVectorDrawable

  init {
    withStyleable(R.styleable.AnimatedVectorDrawableImageButton, attrs) {
      startToEndAnimatedVectorDrawable = getDrawable(
        R.styleable.AnimatedVectorDrawableImageButton_startToEnd
      ) as AnimatedVectorDrawable
      endToStartAnimatedVectorDrawable = getDrawable(
        R.styleable.AnimatedVectorDrawableImageButton_endToStart
      ) as AnimatedVectorDrawable
    }

    setImageDrawable(startToEndAnimatedVectorDrawable)
  }

  override fun performClick(): Boolean {
    morph()

    return super.performClick()
  }

  fun morph() {
    val drawable = if (isAtStartState) {
      startToEndAnimatedVectorDrawable
    } else {
      endToStartAnimatedVectorDrawable
    }

    setImageDrawable(drawable)
    drawable.start()
    isAtStartState = !isAtStartState
  }

  suspend fun morphAndWait() {
    val animatedDrawable = if (isAtStartState) {
      startToEndAnimatedVectorDrawable
    } else {
      endToStartAnimatedVectorDrawable
    }

    isClickable = false

    suspendCancellableCoroutine<Unit> { continuation ->
      val listener = object : Animatable2.AnimationCallback() {
        override fun onAnimationEnd(drawable: Drawable?) {
          animatedDrawable.unregisterAnimationCallback(this)
          continuation.resume(Unit)
        }
      }

      continuation.invokeOnCancellation { animatedDrawable.unregisterAnimationCallback(listener) }

      animatedDrawable.registerAnimationCallback(listener)

      setImageDrawable(animatedDrawable)
      animatedDrawable.start()
    }

    isClickable = true
    isAtStartState = !isAtStartState
  }
}
