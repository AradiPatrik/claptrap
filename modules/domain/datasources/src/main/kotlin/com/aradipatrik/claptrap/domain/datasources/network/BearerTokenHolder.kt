package com.aradipatrik.claptrap.domain.datasources.network

interface BearerTokenHolder {
  fun setToken(token: String)
  fun getToken(): String?
}
