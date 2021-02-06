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

  val jwtIssuer = environment.config.property("jwt.domain").getString()
  val jwtAudience = environment.config.property("jwt.audience").getString()
  val jwtRealm = environment.config.property("jwt.realm").getString()

  val jwkProvider = JwkProviderBuilder(URL("https://www.googleapis.com/oauth2/v3/certs"))
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
      post("/token-sign-in") {
        val payload = call.principal<JWTPrincipal>()?.payload ?: error("JWTPrincipal not found")

        call.application.environment.log.info(payload.claims.entries.joinToString(
          prefix = "{\n",
          postfix = "}\n",
        ) {
          "\"${it.key}\": \"${it.value.asString()}\"\n"
        })

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
