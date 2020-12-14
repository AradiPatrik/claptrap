package com.aradipatrik.claptrap.feature.transactions.mapper

import com.aradipatrik.claptrap.feature.transactions.di.LongMonthDayFormatter
import com.aradipatrik.claptrap.feature.transactions.di.LongYearMonthDayFormatter
import com.aradipatrik.claptrap.feature.transactions.di.MediumYearMonthDayFormatter
import org.joda.time.DateTime
import org.joda.time.MonthDay
import org.joda.time.format.DateTimeFormatter
import javax.inject.Inject

class DateToStringMapper @Inject constructor(
  @LongMonthDayFormatter private val longMonthDayFormatter: DateTimeFormatter,
  @MediumYearMonthDayFormatter private val mediumYearMonthDayFormatter: DateTimeFormatter
) {
  fun mapMediumYearMonthDay(dateTime: DateTime) = dateTime.toString(mediumYearMonthDayFormatter)

  fun mapLongMonthDay(dateTime: DateTime) = dateTime.toString(longMonthDayFormatter)
}