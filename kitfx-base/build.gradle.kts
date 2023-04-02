/*
 *
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.8.10"

    // Apply the java-library plugin for API and implementation separation.
    id("java-library")

    // Apply the javafx plugin
    id("org.openjfx.javafxplugin") version "0.0.13"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Kotlin reflect
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
}

javafx {
    version = "19.0.2.1"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.web")
}

kotlin {
    jvmToolchain(11)
}

