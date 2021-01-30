package com.aradipatrik.claptrap.backend

import com.aradipatrik.claptrap.apimodels.TokenApiModel
import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.request.*
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
      verifier(jwkProvider, jwtIssuer)
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
    post("token-sign-in") {
      val token = call.receive<TokenApiModel>().token
      val decodedJwt = JWT.decode(token)

      print("${decodedJwt.getClaim("family_name")}")
    }
  }
}
