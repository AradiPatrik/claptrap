object Libraries {
  object AndroidX {
    const val appCompat = "androidx.appcompat:appcompat:${Versions.AndroidX.appCompat}"

    object Ktx {
      const val core = "androidx.core:core-ktx:${Versions.AndroidX.coreKtx}"
    }

    object Ui {
      const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.AndroidX.constraintLayout}"

      const val recyclerView =
        "androidx.recyclerview:recyclerview:${Versions.AndroidX.recyclerView}"
    }

    object Navigation {
      const val core =
        "androidx.navigation:navigation-fragment-ktx:${Versions.AndroidX.navigation}"

      const val extensions =
        "androidx.navigation:navigation-ui-ktx:${Versions.AndroidX.navigation}"
    }

    object Lifecycle {
      const val viewModel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.AndroidX.lifecycle}"

      const val lifecycle =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.lifecycle}"

      const val liveData =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.AndroidX.lifecycle}"
    }
  }

  object Dagger {
    const val hilt = "com.google.dagger:hilt-android:${Versions.Dagger.hilt}"
    const val hiltLifecycle =
      "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.Dagger.hiltAndroidX}"
    const val hiltAndroidXKapt =
      "androidx.hilt:hilt-compiler:${Versions.Dagger.hiltAndroidX}"
    const val hiltPlugin = "dagger.hilt.android.plugin"
    const val hiltKapt = "com.google.dagger:hilt-android-compiler:${Versions.Dagger.hilt}"
  }
}
