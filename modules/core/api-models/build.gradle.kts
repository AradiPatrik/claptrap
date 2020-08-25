plugins {
  `kotlin-kapt`
}

dependencies {
  implementation(Libraries.Network.moshi)
  kapt(Libraries.Network.moshiCodegen)
}
