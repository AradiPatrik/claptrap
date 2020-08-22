dependencies {
  implementation(project(":core:domain-models"))
  implementation(project(":domain:datasources"))

  implementation(Libraries.Dagger.hilt)
  implementation(Libraries.Coroutines.core)
}
