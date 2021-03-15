package com.aradipatrik.claptrap.network.interceptor

import com.aradipatrik.claptrap.domain.datasources.disk.UserDiskDataSource
import okhttp3.Interceptor
import okhttp3.Request
import javax.inject.Inject

class BearerTokenAuthenticationInterceptor @Inject constructor(
  private val userDiskDataSource: UserDiskDataSource
) : Interceptor {
  override fun intercept(chain: Interceptor.Chain) = chain
    .proceed(chain.request().addBearerTokenHeaderIfExists())

  private fun Request.addBearerTokenHeaderIfExists() = userDiskDataSource.peakToken()
    ?.let { addBearerToken(it) } ?: this

  private fun Request.addBearerToken(token: String) = newBuilder()
    .addHeader(AUTHORIZATION_HEADER_KEY, "$BEARER $token")
    .build()

  companion object {
    private const val AUTHORIZATION_HEADER_KEY = "Authorization"
    private const val BEARER = "Bearer"
  }
}
