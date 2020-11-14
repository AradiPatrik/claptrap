plugins {
  id(Libraries.Dagger.hiltPlugin)
}

dependencies {
  implementation(project(":navigation"))
  implementation(project(":data:network"))
  implementation(project(":data:disk"))

  implementation(project(":features:todos"))
  implementation(project(":features:transactions"))

  implementation(project(":config"))
  implementation(project(":domain:interactors"))
  implementation(project(":domain:interactor-interfaces"))
  implementation(project(":domain:fake-interactors"))
  implementation(project(":theme"))

  implementation(Libraries.AndroidX.appCompat)
  implementation(Libraries.AndroidX.Ktx.core)
  implementation(Libraries.AndroidX.Ui.constraintLayout)
  implementation(Libraries.AndroidX.Ui.cardView)
  implementation(Libraries.AndroidX.Ui.vectorDrawable)
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
    register("mock") {
      dimension = "environment"
      applicationIdSuffix = ".mock"

      buildConfigField("String", "API_BASE_URL", "\" \"")
    }

    register("dev") {
      dimension = "environment"
      applicationIdSuffix = ".dev"

      buildConfigField("String", "API_BASE_URL", "\"https://hidden-savannah-29279.herokuapp.com/\"")
    }

    register("prod") {
      dimension = "environment"

      buildConfigField("String", "API_BASE_URL", "\" \"")
    }

    sourceSets {
      getByName("mock").java.srcDir("src/mock/kotlin")
      getByName("dev").java.srcDir("src/live/kotlin")
      getByName("prod").java.srcDir("src/live/kotlin")
    }
  }
}
