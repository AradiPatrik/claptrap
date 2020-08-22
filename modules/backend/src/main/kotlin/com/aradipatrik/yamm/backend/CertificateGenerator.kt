package com.aradipatrik.yamm.backend

import java.io.File

object CertificateGenerator {
  @JvmStatic
  fun main(args: Array<String>) {
    val jksFile = File("modules/backend/build/temporary.jks")
    println("Created jsk file at: ${jksFile.absolutePath}")
    io.ktor.network.tls.certificates.generateCertificate(jksFile)
  }
}
