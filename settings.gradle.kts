val includeAndroid: String by extra
val includeKtor: String by extra
val includeSpring: String by extra

if (includeAndroid == "true") {
  include(":app")

  include(":navigation")
  project(":navigation").projectDir = file("./modules/navigation")

  include(":features")
  project(":features").projectDir = file("./modules/features")

  include(":features:transactions")
  project(":features:transactions").projectDir = file("./modules/features/transactions")

  include(":features:statistics")
  project(":features:statistics").projectDir = file("./modules/features/statistics")

  include(":features:wallets")
  project(":features:wallets").projectDir = file("./modules/features/wallets")

  include(":features:common")
  project(":features:common").projectDir = file("./modules/features/common")

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

include(":api")
project(":api").projectDir = file("./api")

include(":core")
project(":core").projectDir = file("./core")

include(":core:api-models")
project(":core:api-models").projectDir = file("./core/api-models")

include(":core:domain-models")
project(":core:domain-models").projectDir = file("./core/domain-models")

include(":core:domain-network-mappers")
project(":core:domain-network-mappers").projectDir = file("./core/domain-network-mappers")

include(":core:json-adapters")
project(":core:json-adapters").projectDir = file("./core/json-adapters")

if (includeKtor == "true") {
  include(":backend-ktor")
  project(":backend-ktor").projectDir = file("./backend-ktor")
}

if (includeSpring == "true") {
  include(":backend-spring")
  project(":backend-spring").projectDir = file("./backend-spring")
}
