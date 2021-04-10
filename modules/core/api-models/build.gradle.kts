plugins {
  `kotlin-kapt`
}

dependencies {
  implementation(Libraries.Network.moshi)
  implementation(Libraries.Money.jodaMoney)
  kapt(Libraries.Network.moshiCodegen)
}
