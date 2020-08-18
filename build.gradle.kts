buildscript {
  repositories {
    google()
    jcenter()
  }

  dependencies {
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.28-alpha")
  }
}

allprojects {
  repositories {
    google()
    jcenter()
  }
}

plugins {
  id("com.aradipatrik.yamm.config")
}

tasks.register("clean", Delete::class) {
  this.group = "clean"

  delete(rootProject.buildDir)
}
