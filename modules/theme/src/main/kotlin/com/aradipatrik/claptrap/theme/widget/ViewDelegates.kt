package com.aradipatrik.claptrap.theme.widget

import kotlin.properties.Delegates

object ViewDelegates {
  fun <T, U> viewGroupObservableProperty(
    views: Lazy<List<T>>,
    initialValue: U,
    onNewValue: T.(oldValue: U, newValue: U) -> Unit
  ) = Delegates.observable(initialValue) { _, oldValue, newValue ->
    views.value.forEach {
      onNewValue(it, oldValue, newValue)
    }
  }
}