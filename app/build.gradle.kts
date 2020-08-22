plugins {
  id(Libraries.Dagger.hiltPlugin)
}

dependencies {
  implementation(project(":navigation"))
  implementation(project(":data:network"))
  implementation(project(":features:todos"))

  implementation(Libraries.AndroidX.appCompat)
  implementation(Libraries.AndroidX.Ktx.core)
  implementation(Libraries.AndroidX.Ui.constraintLayout)
  implementation(Libraries.AndroidX.Navigation.core)
  implementation(Libraries.AndroidX.Navigation.extensions)

  implementation(Libraries.Dagger.hilt)
  implementation(Libraries.Dagger.hiltLifecycle)
  kapt(Libraries.Dagger.hiltKapt)
  kapt(Libraries.Dagger.hiltAndroidXKapt)
}
