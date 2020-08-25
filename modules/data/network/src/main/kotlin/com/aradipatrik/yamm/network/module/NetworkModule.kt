package com.aradipatrik.yamm.network.module

import com.aradipatrik.yamm.config.AppConfig
import com.aradipatrik.yamm.network.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

  @Provides
  @Singleton
  internal fun provideOkHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor
  ) = OkHttpClient.Builder()
    .addInterceptor(httpLoggingInterceptor)
    .build()

  @Provides
  @Singleton
  internal fun provideMoshi() = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

  @Provides
  @Singleton
  fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
    HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
      override fun log(message: String) {
        Timber.tag("OkHttp").d(message)
      }
    }).apply {
      level = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor.Level.BODY
      } else {
        HttpLoggingInterceptor.Level.NONE
      }
    }

  @Provides
  @Singleton
  internal fun provideRetrofit(
    okHttpClient: OkHttpClient,
    moshi: Moshi,
    appConfig: AppConfig
  ): Retrofit = Retrofit.Builder()
    .baseUrl(appConfig.apiBaseUrl)
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()
}
