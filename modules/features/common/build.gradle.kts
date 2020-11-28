plugins {
  id(Libraries.Dagger.hiltPlugin)
}

dependencies {
  implementation(project(":mvi"))

  implementation(Libraries.AndroidX.appCompat)

  implementation(Libraries.Coroutines.core)
  implementation(Libraries.Coroutines.binding)
  implementation(Libraries.Coroutines.materialBinding)

  implementation(Libraries.Logging.timber)

  implementation(Libraries.Dagger.hilt)
  implementation(Libraries.Dagger.hiltLifecycle)
  kapt(Libraries.Dagger.hiltKapt)
  kapt(Libraries.Dagger.hiltAndroidXKapt)
}
