plugins {
    kotlin("jvm") version "2.0.0"
    application
}

group = "io.github.josephsimutis"
version = "1.1.0-pre.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:4.2.2")
    testImplementation(kotlin("test"))
}

application {
    mainClass = "io.github.josephsimutis.MainKt"
}

tasks.test {
    useJUnitPlatform()
}