package com.aradipatrik.claptrap.feature.transactions.list.model

import org.joda.time.YearMonth

object MonthStrings {
  val january get() = YearMonth(2000, 1).toString("MMMM")
  val february get() = YearMonth(2000, 2).toString("MMMM")
  val march get() = YearMonth(2000, 3).toString("MMMM")
  val april get() = YearMonth(2000, 4).toString("MMMM")
  val may get() = YearMonth(2000, 5).toString("MMMM")
  val june get() = YearMonth(2000, 6).toString("MMMM")
  val july get() = YearMonth(2000, 7).toString("MMMM")
  val august get() = YearMonth(2000, 8).toString("MMMM")
  val september get() = YearMonth(2000, 9).toString("MMMM")
  val october get() = YearMonth(2000, 10).toString("MMMM")
  val november get() = YearMonth(2000, 11).toString("MMMM")
  val december get() = YearMonth(2000, 12).toString("MMMM")
}
