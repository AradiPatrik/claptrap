object Libraries {
  object AndroidX {
    const val appCompat = "androidx.appcompat:appcompat:${Versions.AndroidX.appCompat}"

    object Ktx {
      const val core = "androidx.core:core-ktx:${Versions.AndroidX.coreKtx}"
      const val fragment = "androidx.fragment:fragment-ktx:${Versions.AndroidX.fragmentKtx}"
    }

    object Ui {
      const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.AndroidX.constraintLayout}"
      const val recyclerView =
        "androidx.recyclerview:recyclerview:${Versions.AndroidX.recyclerView}"
      const val material = "com.google.android.material:material:${Versions.AndroidX.material}"
      const val cardView = "androidx.cardview:cardview:${Versions.AndroidX.cardView}"
      const val vectorDrawable =
        "androidx.vectordrawable:vectordrawable:${Versions.AndroidX.vectorDrawable}"
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
      const val lifecycleJava8 =
        "androidx.lifecycle:lifecycle-common-java8:${Versions.AndroidX.lifecycle}"
    }

    object Room {
      const val runtime = "androidx.room:room-runtime:${Versions.AndroidX.room}"
      const val ktx = "androidx.room:room-ktx:${Versions.AndroidX.room}"
      const val kapt = "androidx.room:room-compiler:${Versions.AndroidX.room}"
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
    const val assistedInject =
      "com.squareup.inject:assisted-inject-annotations-dagger2:${Versions.Dagger.assistedInject}"
    const val assistedInjectKapt =
      "com.squareup.inject:assisted-inject-processor-dagger2:${Versions.Dagger.assistedInject}"
  }

  object Coroutines {
    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Coroutines.core}"
    const val binding = "ru.ldralighieri.corbind:corbind:${Versions.Coroutines.corbind}"
    const val materialBinding =
      "ru.ldralighieri.corbind:corbind-material:${Versions.Coroutines.corbind}"
  }

  object Network {
    const val okHttp = "com.squareup.okhttp3:okhttp"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.Network.retrofit}"
    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.Network.moshi}"
    const val moshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.Network.moshi}"
    const val loggingInterceptor =
      "com.squareup.okhttp3:logging-interceptor:${Versions.Network.okhttp}"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.Network.retrofit}"
  }

  object Date {
    const val jodaTime = "joda-time:joda-time:${Versions.Date.jodaTime}"
  }

  object Money {
    const val jodaMoney = "org.joda:joda-money:${Versions.Money.jodaMoney}"
  }

  object Logging {
    const val timber = "com.jakewharton.timber:timber:${Versions.Logging.timber}"
  }

  object Ktor {
    const val netty = "io.ktor:ktor-server-netty:${Versions.Ktor.core}"
    const val serverCore = "io.ktor:ktor-server-core:${Versions.Ktor.core}"
    const val htmlBuilder = "io.ktor:ktor-html-builder:${Versions.Ktor.core}"
    const val serverSession = "io.ktor:ktor-server-sessions:${Versions.Ktor.core}"
    const val serverGson = "io.ktor:ktor-gson:${Versions.Ktor.core}"
    const val logback = "ch.qos.logback:logback-classic:${Versions.Ktor.logback}"
    const val networkTls = "io.ktor:ktor-network-tls:${Versions.Ktor.core}"
    const val certificates = "io.ktor:ktor-network-tls-certificates:${Versions.Ktor.core}"
    const val googleApiClient =
      "com.google.api-client:google-api-client:${Versions.Google.apiClient}"

  }

  object Google {
    const val playServices =
      "com.google.android.gms:play-services-auth:${Versions.Google.playServices}"
  }

  object Firebase {
    const val bom = "com.google.firebase:firebase-bom:${Versions.Firebase.bom}"
    const val analytics = "com.google.firebase:firebase-analytics-ktx"
    const val auth = "com.google.firebase:firebase-auth-ktx"
  }
}
