buildscript {
  repositories {
    google()
    jcenter()
  }

  dependencies {
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.28-alpha")
    classpath("com.google.gms:google-services:4.3.4")
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
}

tasks.register("clean", Delete::class) {
  this.group = "clean"

  delete(rootProject.buildDir)
}
