plugins {
  id(Libraries.Dagger.hiltPlugin)
}

dependencies {
  implementation(project(":navigation"))
  implementation(project(":core:domain-models"))
  api(project(":domain:interactors"))
  implementation(project(":mvi"))

  implementation(Libraries.Logging.timber)

  implementation(Libraries.AndroidX.Navigation.core)
  implementation(Libraries.AndroidX.Navigation.extensions)
  implementation(Libraries.AndroidX.Ui.constraintLayout)
  implementation(Libraries.AndroidX.Ui.recyclerView)
  implementation(Libraries.AndroidX.Ui.material)
  implementation(Libraries.AndroidX.appCompat)
  implementation(Libraries.AndroidX.Lifecycle.viewModel)
  implementation(Libraries.AndroidX.Lifecycle.lifecycle)
  implementation(Libraries.AndroidX.Lifecycle.liveData)

  implementation(Libraries.Coroutines.binding)
  implementation(Libraries.Coroutines.core)

  implementation(Libraries.Dagger.hilt)
  implementation(Libraries.Dagger.hiltLifecycle)
  kapt(Libraries.Dagger.hiltKapt)
  kapt(Libraries.Dagger.hiltAndroidXKapt)
}
