package com.aradipatrik.claptrap.backend

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import java.util.concurrent.TimeUnit

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
  install(CallLogging)

  install(ContentNegotiation) {
    gson {
      setPrettyPrinting()
    }
  }

  val jwtIssuer = environment.config.property("jwt.domain").getString()
  val jwtAudience = environment.config.property("jwt.audience").getString()
  val jwtRealm = environment.config.property("jwt.realm").getString()

  val jwkProvider = JwkProviderBuilder("https://www.googleapis.com/oauth2/v3/certs")
    .cached(10, 24, TimeUnit.HOURS)
    .rateLimited(10, 1, TimeUnit.MINUTES)
    .build()

  install(Authentication) {
    jwt {
      verifier(jwkProvider) {
        withIssuer(jwtIssuer)
        withAudience(jwtAudience)
      }
      realm = jwtRealm
      validate { credentials ->
        if (credentials.payload.audience.contains(jwtAudience))
          JWTPrincipal(credentials.payload)
        else
          null
      }
    }
  }

  routing {
    authenticate {
      get("/is-it-authenticated") {
        call.principal<JWTPrincipal>() ?: error("Fooo")
        call.respond("ok")
      }

      post("/token-sign-in") {
        val principal = call.principal<JWTPrincipal>() ?: error("Fooo")

        val claimsAsString = principal.payload.claims.entries.joinToString {
          "${it.key}: ${it.value.asString()}"
        }

        println("id: ${principal.payload.id}")
        println("claims: $claimsAsString")
      }
    }
  }
}
