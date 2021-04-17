import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id("org.openapi.generator").version("5.1.0")
    id("maven-publish")
}

tasks.register("generate-api-model", GenerateTask::class){
    groupId.set(group.toString())
    version.set(version)

    inputSpec.set("$projectDir/openapi.yaml")
    outputDir.set("$buildDir/api-model")
    globalProperties.put("models", "")
    modelPackage.set("model")
    generatorName.set("java")
}

tasks.register("generate-client-android", GenerateTask::class){
//    dependsOn("generate-api-model")
//    groupId.set(group)
//    version.set(version)

    generatorName.set("kotlin")
    inputSpec.set("$projectDir/openapi.yaml")
    outputDir.set("$buildDir/client-android")
    apiPackage.set("org.openapi.example.api")
    invokerPackage.set("org.openapi.example.invoker")
    library.set("jvm-retrofit2")
    configOptions.put("useCoroutines", "true")
    configOptions.put("serializationLibrary" ,"jackson")
}

tasks.register("generate-server-spring", GenerateTask::class){
//    dependsOn("generate-api-model")
//    groupId.set(group)
//    version.set(version)
    inputSpec.set("$projectDir/openapi.yaml")
    outputDir.set("$buildDir/server-spring")
    apiPackage.set("org.openapi.example.api")
    invokerPackage.set("org.openapi.example.invoker")


    generatorName.set("spring")
    configOptions.put("reactive" ,"true")
    configOptions.put("dateLibrary" ,"threetenbp")
    configOptions.put("interfaceOnly" ,"true")
    configOptions.put("serializationLibrary" ,"jackson")
    configOptions.put("library" ,"spring-boot")
    configOptions.put("skipDefaultInterface" ,"true")
    configOptions.put("useRuntimeException" ,"true")
}
