package com.aradipatrik.claptrap.backend

import com.aradipatrik.claptrap.apimodels.UserWire
import com.aradipatrik.claptrap.backend.Auth.installAuthentication
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.addAdapter
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
  install(CallLogging)
  install(DefaultHeaders)

  install(ContentNegotiation) {
    moshi {
      addAdapter(Rfc3339DateJsonAdapter())
    }
  }

  installAuthentication()
  routing {
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
