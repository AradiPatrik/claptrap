repositories {
  mavenLocal()
}

dependencies {
  implementation(Libraries.Date.jodaTime)
  implementation(Libraries.Money.jodaMoney)
  implementation(project(":core:domain-models"))
  implementation(project(":core:api-models"))

  implementation("com.claptrap:spring:1.0.0")
}
