package com.aradipatrik.claptrap.backend

import com.aradipatrik.claptrap.apimodels.UserWire
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing

object Routes {
  fun Application.installRouting() = routing {
    authenticate("firebase", "google") {
      post("/token-sign-in") {
        val payload = call.principal<JWTPrincipal>()?.payload ?: error("JWTPrincipal not found")

        call.respond(
          UserWire(
            id = payload.subject,
            email = payload.getClaim("email").asString(),
            name = payload.getClaim("name").asString(),
            profilePictureUrl = payload.getClaim("picture").asString()
          )
        )
      }
    }
  }
}
