package com.aradipatrik.claptrap.disk.user.datasource

import android.content.Context
import com.aradipatrik.claptrap.disk.user.dao.UserDao
import com.aradipatrik.claptrap.disk.user.entity.UserEntity
import com.aradipatrik.claptrap.disk.user.mapper.UserMapper.fromEntity
import com.aradipatrik.claptrap.disk.user.mapper.UserMapper.signedInUserFromDomain
import com.aradipatrik.claptrap.domain.User
import com.aradipatrik.claptrap.domain.datasources.disk.UserDiskDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDiskDataSourceImpl @Inject constructor(
  @ApplicationContext private val context: Context,
  private val userDao: UserDao
) : UserDiskDataSource {
  private val sharedPreferences =
    context.getSharedPreferences(BEARER_SHARED_PREFS, Context.MODE_PRIVATE)

  private var bearerToken: String?
    get() = sharedPreferences.getString(BEARER_TOKEN_KEY, null)
    set(value) {
      sharedPreferences.edit()
        .putString(BEARER_TOKEN_KEY, value)
        .apply()
    }

  override suspend fun setToken(token: String) {
    bearerToken = token
  }

  override fun peakToken() = bearerToken

  override suspend fun setSignedInUser(user: User) {
    userDao.insert(UserEntity.signedInUserFromDomain(user))
  }

  override fun getSignedInUser(): Flow<User?> = userDao.getMe()
    .map { User.fromEntity(it) }

  companion object {
    private const val BEARER_SHARED_PREFS = "BEARER_SHARED_PREFS"
    private const val BEARER_TOKEN_KEY = "BEARER_TOKEN_KEY"
  }
}
