include(":app")

include(":navigation")
project(":navigation").projectDir = file("./modules/navigation")

include(":features")
project(":features").projectDir = file("./modules/features")

include(":features:todos")
project(":features:todos").projectDir = file("./modules/features/todos")
