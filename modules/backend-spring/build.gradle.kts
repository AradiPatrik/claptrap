plugins {
    id("org.springframework.boot").version("2.3.1.RELEASE")
    kotlin("plugin.spring") version "1.3.21"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
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
    //implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("io.springfox:springfox-spring-webflux:3.0.0-SNAPSHOT")
    implementation("io.springfox:springfox-swagger-ui:3.0.0-SNAPSHOT")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
