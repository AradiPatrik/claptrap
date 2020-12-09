package com.aradipatrik.claptrap.feature.transactions.list.model

import org.joda.time.YearMonth

object Months {
  val monthNumbersToMonths = hashMapOf(
    1 to YearMonth(2000, 1),
    2 to YearMonth(2000, 2),
    3 to YearMonth(2000, 3),
    4 to YearMonth(2000, 4),
    5 to YearMonth(2000, 5),
    6 to YearMonth(2000, 6),
    7 to YearMonth(2000, 7),
    8 to YearMonth(2000, 8),
    9 to YearMonth(2000, 9),
    10 to YearMonth(2000, 10),
    11 to YearMonth(2000, 11),
    12 to YearMonth(2000, 12),
  )

  val january get() = monthNumbersToMonths[1]!!
  val february get() = monthNumbersToMonths[2]!!
  val march get() = monthNumbersToMonths[3]!!
  val april get() = monthNumbersToMonths[4]!!
  val may get() = monthNumbersToMonths[5]!!
  val june get() = monthNumbersToMonths[6]!!
  val july get() = monthNumbersToMonths[7]!!
  val august get() = monthNumbersToMonths[8]!!
  val september get() = monthNumbersToMonths[9]!!
  val october get() = monthNumbersToMonths[10]!!
  val november get() = monthNumbersToMonths[11]!!
  val december get() = monthNumbersToMonths[12]!!
}
