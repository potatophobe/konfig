@file:Suppress("UnstableApiUsage")

pluginManagement {
    val nexusStagingVersion: String by settings
    val kotlinVersion: String by settings
    val kspVersion: String by settings

    plugins {
        id("com.google.devtools.ksp") version kspVersion
        kotlin("jvm") version kotlinVersion
        id("io.codearte.nexus-staging") version  nexusStagingVersion
    }
}

dependencyResolutionManagement {
    val kspVersion: String by settings
    val kotlinpoetVersion: String by settings

    repositories {
        mavenLocal()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            library("ksp-api", "com.google.devtools.ksp:symbol-processing-api:$kspVersion")
            library("kotlinpoet", "com.squareup:kotlinpoet:$kotlinpoetVersion")
            library("kotlinpoet-ksp", "com.squareup:kotlinpoet-ksp:$kotlinpoetVersion")
        }
    }
}

rootProject.name = "konfig"
include("api", "impl", "kts", "utils", "full", "ksp", "sample")
