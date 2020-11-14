package com.aradipatrik.claptrap.theme.widget

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.aradipatrik.claptrap.theme.R


class AnimatedVectorDrawableImageButton@JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  @AttrRes defStyleAttr: Int = 0
) : AppCompatImageButton(context, attrs, defStyleAttr) {
  private var isAtStartState = true

  private var startToEndAnimatedVectorDrawable: AnimatedVectorDrawable
  private var endToStartAnimatedVectorDrawable: AnimatedVectorDrawable

  init {
    context.theme.obtainStyledAttributes(
      attrs,
      R.styleable.AnimatedVectorDrawableImageButton,
      0, 0
    ).apply {
      try {
        startToEndAnimatedVectorDrawable = getDrawable(
          R.styleable.AnimatedVectorDrawableImageButton_startToEnd
        ) as AnimatedVectorDrawable
        endToStartAnimatedVectorDrawable = getDrawable(
          R.styleable.AnimatedVectorDrawableImageButton_endToStart
        ) as AnimatedVectorDrawable
      } finally {
        recycle()
      }
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
}