package com.aradipatrik.claptrap.backend

import com.aradipatrik.claptrap.apimodels.UserWire
import com.auth0.jwk.JwkProviderBuilder
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import java.net.URL
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

  val googleJwtIssuer = environment.config.property("jwt.google.domain").getString()
  val googleJwtAudience = environment.config.property("jwt.google.audience").getString()

  val firebaseJwtIssuer = environment.config.property("jwt.firebase.domain").getString()
  val firebaseJwtAudience = environment.config.property("jwt.firebase.audience").getString()

  val jwtRealm = environment.config.property("jwt.google.realm").getString()

  val googleJwkProvider = JwkProviderBuilder(URL("https://www.googleapis.com/oauth2/v3/certs"))
    .cached(10, 24, TimeUnit.HOURS)
    .rateLimited(10, 1, TimeUnit.MINUTES)
    .build()

  val firebaseJwkProvider = JwkProviderBuilder(URL(
    "https://www.googleapis.com/service_accounts/v1/jwk/securetoken@system.gserviceaccount.com"
  )).cached(10, 24, TimeUnit.HOURS)
    .rateLimited(10, 1, TimeUnit.MINUTES)
    .build()

  install(Authentication) {
//    jwt("google") {
//      verifier(googleJwkProvider) {
//        withIssuer(googleJwtIssuer)
//        withAudience(googleJwtAudience)
//      }
//      realm = jwtRealm
//      validate { credentials ->
//        if (credentials.payload.audience.contains(googleJwtAudience))
//          JWTPrincipal(credentials.payload)
//        else
//          null
//      }
//    }

    jwt("firebase") {
      verifier(firebaseJwkProvider) {
        withIssuer(firebaseJwtIssuer)
        withAudience(firebaseJwtAudience)
      }
      realm = jwtRealm
      validate { credentials ->
        if (credentials.payload.audience.contains(firebaseJwtAudience))
          JWTPrincipal(credentials.payload)
        else
          null
      }
    }
  }

  routing {
    authenticate("firebase") {
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
