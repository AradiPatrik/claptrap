package com.aradipatrik.yamm.di

import com.aradipatrik.yamm.BuildConfig
import com.aradipatrik.yamm.config.AppConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
class ConfigModule {
  @Provides
  fun provideAppConfig() = AppConfig(
    apiBaseUrl = BuildConfig.API_BASE_URL
  )
}
