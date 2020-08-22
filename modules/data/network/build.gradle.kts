plugins {
  id(Libraries.Dagger.hiltPlugin)
}

dependencies {
  api(project(":domain:datasources"))
  implementation(project(":core:domain-models"))
  implementation(project(":core:api-models"))

  implementation(Libraries.Dagger.hilt)
  kapt(Libraries.Dagger.hiltKapt)

  implementation(Libraries.Coroutines.core)

  api(Libraries.Network.moshi)
  kapt(Libraries.Network.moshiCodegen)
  implementation(Libraries.Network.moshiConverter)
  api(Libraries.Network.okHttp)
  api(Libraries.Network.retrofit)

}
