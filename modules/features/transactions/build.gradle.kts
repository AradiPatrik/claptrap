plugins {
  id(Libraries.Dagger.hiltPlugin)
}

dependencies {
  implementation(project(":theme"))
  implementation(project(":navigation"))
  implementation(project(":core:domain-models"))
  implementation(project(":mvi"))
  implementation(project(":domain:interactor-interfaces"))

  implementation(Libraries.AndroidX.appCompat)
  implementation(Libraries.AndroidX.Ui.constraintLayout)
  implementation(Libraries.AndroidX.Ui.cardView)
  implementation(Libraries.AndroidX.Ui.recyclerView)

  implementation(Libraries.AndroidX.Lifecycle.viewModel)
  implementation(Libraries.AndroidX.Lifecycle.lifecycle)
  implementation(Libraries.AndroidX.Ktx.core)
  implementation(Libraries.AndroidX.Ktx.fragment)


  implementation(Libraries.Coroutines.core)
  implementation(Libraries.Coroutines.binding)
  implementation(Libraries.Coroutines.materialBinding)

  implementation(Libraries.Dagger.hilt)
  implementation(Libraries.Dagger.hiltLifecycle)
  kapt(Libraries.Dagger.hiltKapt)
  kapt(Libraries.Dagger.hiltAndroidXKapt)
}
