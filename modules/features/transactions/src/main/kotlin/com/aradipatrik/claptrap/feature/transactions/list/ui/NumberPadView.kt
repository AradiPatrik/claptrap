package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import com.aradipatrik.claptrap.feature.transactions.databinding.ViewNumberPadBinding
import com.aradipatrik.claptrap.theme.widget.ViewUtil.inflateAndAddUsing
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import ru.ldralighieri.corbind.view.clicks
import kotlin.properties.Delegates

class NumberPadView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
  private val binding = inflateAndAddUsing(ViewNumberPadBinding::inflate)

  val digitClicks = merge(
    binding.numberPadNumber0.clicks().map { 0 },
    binding.numberPadNumber1.clicks().map { 1 },
    binding.numberPadNumber2.clicks().map { 2 },
    binding.numberPadNumber3.clicks().map { 3 },
    binding.numberPadNumber4.clicks().map { 4 },
    binding.numberPadNumber5.clicks().map { 5 },
    binding.numberPadNumber6.clicks().map { 6 },
    binding.numberPadNumber7.clicks().map { 7 },
    binding.numberPadNumber8.clicks().map { 8 },
    binding.numberPadNumber9.clicks().map { 9 },
  )

  val plusClicks = binding.numberPadNumberPlus.clicks()

  val minusClicks = binding.numberPadNumberMinus.clicks()

  val pointClicks = binding.numberPadPoint.clicks()

  val deleteOneClicks = binding.numberPadDeleteOne.clicks()

  val actionClicks = binding.numberPadAction.clicks()

  var calculatorDisplayText: String by Delegates.observable("") { _, _, newValue ->
    binding.expressionDisplay.text = newValue
  }
}
