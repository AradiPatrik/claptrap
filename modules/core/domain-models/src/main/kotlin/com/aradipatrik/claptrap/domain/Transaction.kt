package com.aradipatrik.claptrap.domain

import org.joda.money.Money
import org.joda.time.DateTime

data class Transaction(
  val id: String,
  val money: Money,
  val date: DateTime,
  val memo: String,
  val category: Category,
)
