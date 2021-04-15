plugins {
  `kotlin-kapt`
}

dependencies {
  implementation(project(":core:domain-models"))
  implementation(project(":core:api-models"))

  implementation(Libraries.Network.moshi)
  implementation(Libraries.Network.moshiAdapter)
  implementation(Libraries.Money.jodaMoney)
  kapt(Libraries.Network.moshiCodegen)
}
