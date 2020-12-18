package com.aradipatrik.claptrap.theme.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.AnimatedVectorDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


object ViewThemeUtil {
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

  fun <T : ViewBinding> ViewGroup.inflateAndAddUsing(inflaterMethod: (LayoutInflater) -> T) =
    inflaterMethod(LayoutInflater.from(context)).apply {
      addView(root)
    }

  fun Context.getDimenValue(
    @DimenRes dimen: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
  ): Float {
    resources.getValue(dimen, typedValue, resolveRefs)
    return typedValue.float
  }

  fun Fragment.getAnimatedVectorDrawable(@DrawableRes drawable: Int) =
    ContextCompat.getDrawable(requireContext(), drawable) as AnimatedVectorDrawable

  suspend fun <T> MaterialDatePicker<T>.showAndWaitWith(
    fragmentManager: FragmentManager
  ) = suspendCancellableCoroutine<T> { continuation ->
    addOnPositiveButtonClickListener { continuation.resume(it) }
    addOnDismissListener { continuation.cancel() }
    addOnCancelListener { continuation.cancel() }
    addOnNegativeButtonClickListener { continuation.cancel() }
    show(fragmentManager, "MATERIAL_DATE_PICKER")
  }
}