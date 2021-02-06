package com.aradipatrik.claptrap.network.todo.holder

import android.content.Context
import com.aradipatrik.claptrap.domain.datasources.network.BearerTokenHolder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BearerTokenHolderImpl @Inject constructor(
  @ApplicationContext private val context: Context
) : BearerTokenHolder {
  private val sharedPreferences =
    context.getSharedPreferences(BEARER_SHARED_PREFS, Context.MODE_PRIVATE)

  private var bearerToken: String?
    get() = sharedPreferences.getString(BEARER_TOKEN_KEY, null)
    set(value) {
      sharedPreferences.edit()
        .putString(BEARER_TOKEN_KEY, value)
        .apply()
    }

  override fun setToken(token: String) {
    bearerToken = token
  }

  override fun getToken() = bearerToken

  companion object {
    private const val BEARER_SHARED_PREFS = "BEARER_SHARED_PREFS"
    private const val BEARER_TOKEN_KEY = "BEARER_TOKEN_KEY"
  }
}
