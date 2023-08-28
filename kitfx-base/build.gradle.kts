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

    // Plugin for publishing
    id("maven-publish")
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

group = "com.github.maroc81"
version = "0.1"

kotlin {
    jvmToolchain(11)
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = "https://maven.pkg.github.com/maroc81/kitfx"
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.maroc81"
            artifactId = "kitfx-base"
            version = "0.1"

            from(components["java"])
        }
    }
}

