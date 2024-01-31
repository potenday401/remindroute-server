val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

val exposed_version: String by project
val h2_version: String by project

plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.7"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

group = "org.potenday401"
version = "0.0.1"

application {
    mainClass.set("org.potenday401.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ktor {
    fatJar {
        archiveFileName.set("remindroute.jar")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-client-core-jvm")
    implementation("io.ktor:ktor-client-apache-jvm")
    implementation("io.ktor:ktor-server-resources")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-default-headers-jvm")
    implementation("io.ktor:ktor-server-swagger-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-gson-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation ("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:0.30.1")
    implementation("com.h2database:h2:$h2_version")
    implementation("io.ktor:ktor-server-thymeleaf-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("mysql:mysql-connector-java:8.0.20")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("com.sun.mail:javax.mail:1.6.2")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("org.mockito:mockito-core:5.9.0")


}
