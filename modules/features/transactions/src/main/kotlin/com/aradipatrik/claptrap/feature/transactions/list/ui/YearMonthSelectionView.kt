package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import com.aradipatrik.claptrap.feature.transactions.databinding.ViewYearMonthSelectionBinding
import com.aradipatrik.claptrap.theme.widget.ViewUtil.inflateAndAddUsing

class YearMonthSelectionView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
  private val binding = inflateAndAddUsing(ViewYearMonthSelectionBinding::inflate)


}