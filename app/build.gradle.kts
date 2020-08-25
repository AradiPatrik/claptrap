plugins {
  id(Libraries.Dagger.hiltPlugin)
}

dependencies {
  implementation(project(":navigation"))
  implementation(project(":data:network"))
  implementation(project(":data:disk"))
  implementation(project(":features:todos"))
  implementation(project(":config"))

  implementation(Libraries.AndroidX.appCompat)
  implementation(Libraries.AndroidX.Ktx.core)
  implementation(Libraries.AndroidX.Ui.constraintLayout)
  implementation(Libraries.AndroidX.Navigation.core)
  implementation(Libraries.AndroidX.Navigation.extensions)

  implementation(Libraries.Logging.timber)

  implementation(Libraries.Dagger.hilt)
  implementation(Libraries.Dagger.hiltLifecycle)
  kapt(Libraries.Dagger.hiltKapt)
  kapt(Libraries.Dagger.hiltAndroidXKapt)
}

android {
  productFlavors {
    flavorDimensions("environment")
    register("dev") {
      applicationIdSuffix = ".dev"

      buildConfigField("String", "API_BASE_URL", "\"https://hidden-savannah-29279.herokuapp.com/\"")
    }

    register("prod") {
      buildConfigField("String", "API_BASE_URL", " ")
    }
  }
}
