package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import com.aradipatrik.claptrap.feature.transactions.databinding.ViewYearMonthSelectionBinding
import com.aradipatrik.claptrap.feature.transactions.list.model.Months
import com.aradipatrik.claptrap.theme.widget.ViewUtil.inflateAndAddUsing
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import org.joda.time.YearMonth
import ru.ldralighieri.corbind.material.checkedChanges
import kotlin.properties.Delegates

class YearMonthSelectionView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
  private val binding = inflateAndAddUsing(ViewYearMonthSelectionBinding::inflate)

  init {
    with(binding) {
      monthButtonJanuary.text = Months.january.monthString
      monthButtonFebruary.text = Months.february.monthString
      monthButtonMarch.text = Months.march.monthString
      monthButtonApril.text = Months.april.monthString
      monthButtonMay.text = Months.may.monthString
      monthButtonJune.text = Months.june.monthString
      monthButtonJuly.text = Months.july.monthString
      monthButtonAugust.text = Months.august.monthString
      monthButtonSeptember.text = Months.september.monthString
      monthButtonOctober.text = Months.october.monthString
      monthButtonNovember.text = Months.november.monthString
      monthButtonDecember.text = Months.december.monthString
    }
  }

  private val YearMonth.monthString get() = toString("MMMM")

  var selectedMonth: Int by Delegates.observable(1) { _, oldValue, newValue ->
    if (oldValue != newValue && !getButtonForMonthNumber(newValue).isChecked) {
      getButtonForMonthNumber(newValue).isChecked = true
    }
  }

  private fun getButtonForMonthNumber(monthNumber: Int) = with(binding) {
    when (monthNumber) {
      1 -> monthButtonJanuary
      2 -> monthButtonFebruary
      3 -> monthButtonMarch
      4 -> monthButtonApril
      5 -> monthButtonMay
      6 -> monthButtonJune
      7 -> monthButtonJuly
      8 -> monthButtonAugust
      9 -> monthButtonSeptember
      10 -> monthButtonOctober
      11 -> monthButtonNovember
      12 -> monthButtonDecember
      else -> error("Invalid month number $monthNumber")
    }
  }

  val monthClicks get() = binding.monthChipGroup.checkedChanges()
    .map { id ->
      1.rangeTo(12)
        .first { getButtonForMonthNumber(it).id == id }
    }
    .drop(1)
}
