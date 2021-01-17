package com.aradipatrik.claptrap.network.todo.datasource

import com.aradipatrik.claptrap.apimodels.TokenApiModel
import com.aradipatrik.claptrap.domain.User
import com.aradipatrik.claptrap.domain.datasources.network.UserNetworkDataSource
import com.aradipatrik.claptrap.network.todo.api.UserApi
import javax.inject.Inject

class UserNetworkDataSourceImpl @Inject constructor(
  private val userApi: UserApi
) : UserNetworkDataSource {
  override suspend fun signInWithGoogleToken(token: String) = userApi.signInWithToken(
    TokenApiModel(token)
  ).let {
    User(
      idToken = token,
      id = it.id,
      name = it.email,
      profilePictureUri = null
    )
  }
}