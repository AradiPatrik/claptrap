package com.aradipatrik.claptrap.apimodels

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetTransactionsResponse(
  val transactions: List<TransactionWire>,
)
