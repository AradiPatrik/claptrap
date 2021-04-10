plugins {
    id("org.springframework.boot").version("2.3.3.RELEASE")
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
}

tasks.withType<Jar> {
    archiveBaseName.set("bug-free-octo-spoon")
}

repositories {
    mavenCentral()
    maven(url = "https://repo.spring.io/snapshot")
    maven(url = "https://repo.spring.io/milestone")
    maven(url = "https://oss.jfrog.org/artifactory/oss-snapshot-local/")
}

dependencies {
    implementation(project(":core:api-models"))
    implementation(project(":core:domain-models"))
    implementation(project(":core:domain-network-mappers"))
    implementation(project(":core:json-adapters"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")

    implementation("javax.servlet:javax.servlet-api")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
