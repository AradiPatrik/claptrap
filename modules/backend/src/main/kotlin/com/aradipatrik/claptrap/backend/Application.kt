package com.aradipatrik.claptrap.backend

import com.aradipatrik.claptrap.apimodels.AddTransactionResponse
import com.aradipatrik.claptrap.apimodels.GetTransactionsResponse
import com.aradipatrik.claptrap.apimodels.TransactionWire
import com.aradipatrik.claptrap.backend.Auth.installAuthentication
import com.aradipatrik.claptrap.backend.Routes.installRouting
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.addAdapter
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

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

  Database.connect(hikari())
  transaction {
    addLogger(StdOutSqlLogger)
    SchemaUtils.createMissingTablesAndColumns(Transactions)
  }

  installAuthentication()
  installRouting()

  routing {
    route("transactions") {
      post {
        val transactionWire = call.receive<TransactionWire>()
        val insertedId = dbQuery {
          Transactions.insert {
            it[id] = transactionWire.id ?: UUID.randomUUID().toString()
            it[memo] = transactionWire.memo
          } get Transactions.id
        }
        call.respond(AddTransactionResponse(insertedId))
      }

      get {
        call.respond(dbQuery {
          GetTransactionsResponse(
            Transactions.selectAll()
              .map {
                TransactionWire(
                  it[Transactions.id],
                  it[Transactions.memo],
                )
              }
          )
        })
      }
    }
  }
}

suspend inline fun <reified T> dbQuery(crossinline block: () -> T): T =
  withContext(Dispatchers.IO) {
    transaction {
      addLogger(StdOutSqlLogger)
      block()
    }
  }

object Transactions : Table() {
  val id = varchar(name = "id", length = 36)
  val memo = varchar(name = "memo", length = 50)

  override val primaryKey = PrimaryKey(id)
}

private fun hikari(): HikariDataSource {
  val config = HikariConfig()
  config.driverClassName = "org.postgresql.Driver"
  config.jdbcUrl = System.getenv("JDBC_DATABASE_URL")
  config.maximumPoolSize = 3
  config.isAutoCommit = false
  config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
  config.validate()
  return HikariDataSource(config)
}
