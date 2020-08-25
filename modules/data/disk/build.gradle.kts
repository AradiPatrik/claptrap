plugins {
  `kotlin-kapt`
  id(Libraries.Dagger.hiltPlugin)
}

dependencies {
  implementation(project(":core:domain-models"))
  implementation(project(":domain:datasources"))

  implementation(Libraries.Dagger.hilt)
  kapt(Libraries.Dagger.hiltKapt)

  implementation(Libraries.Coroutines.core)

  implementation(Libraries.AndroidX.Room.runtime)
  implementation(Libraries.AndroidX.Room.ktx)
  kapt(Libraries.AndroidX.Room.kapt)
}
