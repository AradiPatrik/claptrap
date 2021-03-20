package com.aradipatrik.claptrap.backend

import com.aradipatrik.claptrap.apimodels.UserWire
import com.aradipatrik.claptrap.backend.Auth.installAuthentication
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
  install(CallLogging)
  install(DefaultHeaders)

  install(ContentNegotiation) {
    moshi {
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
