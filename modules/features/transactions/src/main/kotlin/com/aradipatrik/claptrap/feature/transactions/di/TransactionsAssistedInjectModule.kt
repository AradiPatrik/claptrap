package com.aradipatrik.claptrap.feature.transactions.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@InstallIn(FragmentComponent::class)
@AssistedModule
@Module
interface TransactionsAssistedInjectModule
