package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.constraintlayout.motion.widget.MotionLayout
import com.aradipatrik.claptrap.feature.transactions.R
import com.aradipatrik.claptrap.feature.transactions.databinding.ViewAnimatedHeaderBinding
import com.aradipatrik.claptrap.theme.widget.ViewUtil.inflateAndAddUsing
import kotlin.properties.Delegates

class AnimatedHeaderView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
  private val binding = inflateAndAddUsing(ViewAnimatedHeaderBinding::inflate)
  private var upperText: String by Delegates.observable("") { _, _, text ->
    binding.upperView.text = text
  }
  private var lowerText: String by Delegates.observable("") { _, _, text ->
    binding.lowerView.text = text
  }

  fun setText(text: String) {
    upperText = lowerText
    lowerText = text
    binding.motionLayout.progress = 0.0f
    binding.motionLayout.transitionToEnd()
  }
}
