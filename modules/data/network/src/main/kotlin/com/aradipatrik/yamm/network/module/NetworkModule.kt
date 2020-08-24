package com.aradipatrik.yamm.network.module

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

  // This is only for debugging, should be removed on production!!!
  @Provides
  @Singleton
  internal fun provideOkHttpClient() = OkHttpClient.Builder()
    .build()

  @Provides
  @Singleton
  internal fun provideMoshi() = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

  @Provides
  @Singleton
  internal fun provideRetrofit(
    okHttpClient: OkHttpClient,
    moshi: Moshi
  ): Retrofit = Retrofit.Builder()
    .baseUrl("https://10.0.2.2:8443")
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()
}
