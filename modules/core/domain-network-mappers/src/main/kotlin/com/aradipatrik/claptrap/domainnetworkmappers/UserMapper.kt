package com.aradipatrik.claptrap.domainnetworkmappers

import com.aradipatrik.claptrap.apimodels.UserWire
import com.aradipatrik.claptrap.domain.User

object UserMapper {
  @JvmName("toWireInstance")
  fun User.toWire() = UserWire(
    id = id,
    name = name,
    email = email,
    profilePictureUrl = profilePictureUri,
  )

  fun toWire(user: User) = user.toWire()
}
