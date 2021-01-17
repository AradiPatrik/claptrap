package com.aradipatrik.claptrap.backend

import com.aradipatrik.claptrap.apimodels.TodoApiModel
import com.aradipatrik.claptrap.apimodels.TokenApiModel
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.html.respondHtml
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.util.KtorExperimentalAPI
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.html.body
import kotlinx.html.h1
import java.util.*

const val WEB_CLIENT_ID = "93355315460-nf3aqe9s763a521hgp3a1mrpkv9sc10r.apps.googleusercontent.com"
const val ANDROID_CLIENT_ID =
  "93355315460-hslms3ov0178h4g3l9g8k5emp4q4jp8m.apps.googleusercontent.com"

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

  routing {
    post("token-sign-in") {
      val token = call.receive<TokenApiModel>().token
      val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), JacksonFactory())
        .setAudience(listOf(WEB_CLIENT_ID, ANDROID_CLIENT_ID))
        .build()

      var verifiedToken: GoogleIdToken?
      withContext(Dispatchers.IO) {
        verifiedToken = verifier.verify(token)
      }

      verifiedToken?.let {
        val payload = it.payload
        val email = payload.email
        val name = payload["name"]
        val pictureUrl = payload["picture"]
        val locale = payload["locale"]
        val familyName = payload["family_name"]
        val givenName = payload["given_name"]

        println("User ID: ${payload.subject}")
        println("User email: $email")
        println("User name: $name")
        println("User pictureUrl: $pictureUrl")
        println("User locale: $locale")
        println("User familyName: $familyName")
        println("User givenName: $givenName")
      } ?: println("Invalid ID token")
    }
  }
}
