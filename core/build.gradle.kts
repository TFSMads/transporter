version = "0.1.0"

plugins {
    id("java-library")
}

dependencies {
    api(project(":api"))
    implementation("org.jetbrains:annotations:24.0.0")
    compileOnly("com.google.code.gson:gson:2.10.1")
    compileOnly("org.apache.commons:commons-lang3:3.0")
    compileOnly("commons-io:commons-io:2.6")
}

labyModProcessor {
    referenceType = net.labymod.gradle.core.processor.ReferenceType.DEFAULT
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}