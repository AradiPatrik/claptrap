package com.aradipatrik.claptrap.network.todo.api

import com.aradipatrik.claptrap.apimodels.TokenApiModel
import com.aradipatrik.claptrap.apimodels.UserApiModel
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
  @POST("token-sign-in")
  suspend fun signInWithToken(@Body token: TokenApiModel): UserApiModel
}
