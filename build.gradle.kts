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
  id("com.aradipatrik.claptrap.config")
}

tasks.register("clean", Delete::class) {
  this.group = "clean"

  delete(rootProject.buildDir)
}

tasks.register("build-backend") {
  dependsOn(":backend:build")
}

tasks.register("clean-backend") {
  dependsOn(":backend:clean")
}

tasks.getByName("build-backend")
  .mustRunAfter("clean-backend")

tasks.register("stage") {
  dependsOn("build-backend", "clean-backend")
}
