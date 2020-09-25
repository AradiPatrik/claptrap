package com.aradipatrik.claptrap.backend

import com.aradipatrik.claptrap.apimodels.TodoApiModel
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.html.respondHtml
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import kotlinx.html.body
import kotlinx.html.h1
import java.util.*

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
    get("/todos") {
      call.respond(listOf(
        TodoApiModel(UUID.randomUUID().toString(), "Vidd ki a szemetet", isDone = true),
        TodoApiModel(UUID.randomUUID().toString(), "Fozz vacsorat", isDone = true),
        TodoApiModel(UUID.randomUUID().toString(), "Tanulj nemetul", isDone = false),
        TodoApiModel(UUID.randomUUID().toString(), "Kodoljal", isDone = true),
      ))
    }

    get("/test") {
      call.respondHtml {
        body {
          h1 { +"This is working" }
        }
      }
    }

    get("/foos") {
      call.respondHtml {
        body {
          h1 {
            +"This should work!!!!"
          }
        }
      }
    }
  }
}



