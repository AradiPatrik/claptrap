package com.aradipatrik.claptrap.di

import com.aradipatrik.claptrap.interactors.interfaces.todo.TodoInteractor
import com.aradipatrik.claptrap.interactors.todo.TodoInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class InteractorModule {
  @Binds
  abstract fun bindTodoInteractor(interactor: TodoInteractorImpl): TodoInteractor
}