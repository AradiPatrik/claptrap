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
  delete(rootProject.buildDir)
}
