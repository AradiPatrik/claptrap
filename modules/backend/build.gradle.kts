plugins {
  application
  id("com.github.johnrengelman.shadow") version "5.0.0"
}

application {
  group = "com.aradipatrik"
  version = "1.0.0"
  mainClassName = "io.ktor.server.netty.EngineMain"
}

dependencies {
  implementation(project(":core:api-models"))

  implementation(Libraries.Ktor.netty)
  implementation(Libraries.Ktor.logback)
  implementation(Libraries.Ktor.htmlBuilder)
  implementation(Libraries.Ktor.serverCore)
  implementation(Libraries.Ktor.serverGson)
  implementation(Libraries.Ktor.serverSession)
  implementation(Libraries.Ktor.networkTls)
  implementation(Libraries.Ktor.certificates)
}

tasks.withType<Jar> {
  manifest {
    attributes(
      mapOf(
        "Main-Class" to application.mainClassName
      )
    )
  }
}
