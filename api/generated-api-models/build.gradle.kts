plugins {
  id("maven-publish")
  `java-library`
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      groupId = "com.claptrap"
      artifactId = "apimodels"
      version = "1.0.0"

      artifact("$buildDir/libs/generated-api-models.jar")
    }
  }
}

tasks.register("cleanup", Delete::class) {
  delete = setOf("src", "docs")
}

tasks.register("build-and-publish") {
  group = "openapi-generator"

  dependsOn("jar")
  dependsOn("publishToMavenLocal")
}

dependencies {
  implementation("com.fasterxml.jackson.core:jackson-core:2.12.3")
  implementation("com.fasterxml.jackson.core:jackson-annotations:2.12.3")
}
