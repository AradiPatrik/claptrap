dependencies {
  implementation(Libraries.Date.jodaTime)
  implementation(Libraries.Money.jodaMoney)
  implementation(project(":core:domain-models"))
  implementation(project(":core:api-models"))
}
