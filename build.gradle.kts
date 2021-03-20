buildscript {
  repositories {
    google()
    jcenter()
  }

  dependencies {
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.29.1-alpha")
    classpath("com.google.gms:google-services:4.3.5")
  }
}

allprojects {
  repositories {
    google()
    jcenter()
  }
}

plugins {
  id("com.aradipatrik.claptrap.config")
  id("io.gitlab.arturbosch.detekt").version("1.16.0")
}

subprojects {
  apply(plugin = "io.gitlab.arturbosch.detekt")

  detekt {
    toolVersion = "1.16.0"
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config = files("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt

    reports {
      html.enabled = true // observe findings in your browser with structure and code snippets
      xml.enabled = true // checkstyle like format mainly for integrations like Jenkins
      txt.enabled = true // similar to the console output, contains issue signature to manually edit baseline files
      sarif.enabled = true // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with Github Code Scanning
    }
  }
}

tasks.register("clean", Delete::class) {
  this.group = "clean"

  delete(rootProject.buildDir)
}

// Kotlin DSL
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
  // Target version of the generated JVM bytecode. It is used for type resolution.
  jvmTarget = "1.8"
}
