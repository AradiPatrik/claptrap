val includeAndroid: String by extra
val includeKtor: String by extra
val includeSpring: String by extra

if (includeAndroid == "true") {
  include(":app")

  include(":navigation")
  project(":navigation").projectDir = file("./modules/navigation")

  include(":features")
  project(":features").projectDir = file("./modules/features")

  include(":features:todos")
  project(":features:todos").projectDir = file("./modules/features/todos")

  include(":features:transactions")
  project(":features:transactions").projectDir = file("./modules/features/transactions")

  include(":data")
  project(":data").projectDir = file("./modules/data")

  include(":data:disk")
  project(":data:disk").projectDir = file("./modules/data/disk")

  include(":data:network")
  project(":data:network").projectDir = file("./modules/data/network")

  include(":domain")
  project(":domain").projectDir = file("./modules/domain")

  include(":domain:datasources")
  project(":domain:datasources").projectDir = file("./modules/domain/datasources")

  include(":domain:interactors")
  project(":domain:interactors").projectDir = file("./modules/domain/interactors")

  include(":domain:interactor-interfaces")
  project(":domain:interactor-interfaces").projectDir = file("./modules/domain/interactor-interfaces")

  include(":domain:fake-interactors")
  project(":domain:fake-interactors").projectDir = file("./modules/domain/fake-interactors")

  include(":config")
  project(":config").projectDir = file("./modules/config")

  include(":theme")
  project(":theme").projectDir = file("./modules/theme")

  include(":mvi")
  project(":mvi").projectDir = file("./modules/mvi")
}

include(":core")
project(":core").projectDir = file("./modules/core")

include(":core:api-models")
project(":core:api-models").projectDir = file("./modules/core/api-models")

include(":core:domain-models")
project(":core:domain-models").projectDir = file("./modules/core/domain-models")

if (includeKtor == "true") {
  include(":backend")
  project(":backend").projectDir = file("./modules/backend")
}

if (includeSpring == "true") {
  include(":backend-spring")
  project(":backend-spring").projectDir = file("./modules/backend-spring")
}
