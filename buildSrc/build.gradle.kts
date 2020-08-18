plugins {
  `kotlin-dsl`
}

gradlePlugin {
  plugins {
    create("config") {
      id = "com.aradipatrik.yamm.config"
      implementationClass = "com.aradipatrik.yamm.plugin.ProjectConfigurationPlugin"
    }
  }
}

repositories {
  google()
  jcenter()
}

dependencies {
  // Gradle plugin dependencies
  implementation(gradleApi())

  // Android plugin dependencies
  implementation("com.android.tools.build:gradle:4.0.1") // can't use constants here

  // Kotlin plugin dependencies
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.0")
}
