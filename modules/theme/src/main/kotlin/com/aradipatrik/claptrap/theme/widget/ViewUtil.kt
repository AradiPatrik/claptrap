package com.aradipatrik.claptrap.theme.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes


object ViewUtil {
  fun View.withStyleable(
    styleable: IntArray,
    attrs: AttributeSet?,
    block: TypedArray.() -> Unit
  ) {
    context.theme.obtainStyledAttributes(attrs, styleable, 0, 0).apply {
      try {
        this.block()
      } finally {
        recycle()
      }
    }
  }

  fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    View.inflate(context, layoutRes, this)

  fun Context.getDimenValue(
    @DimenRes dimen: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
  ): Float {
    resources.getValue(dimen, typedValue, resolveRefs)
    return typedValue.float
  }
}