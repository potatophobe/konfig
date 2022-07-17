@file:Suppress("UnstableApiUsage")

pluginManagement {
    val kotlinVersion: String by settings
    val kspVersion: String by settings

    plugins {
        id("com.google.devtools.ksp") version kspVersion
        kotlin("jvm") version kotlinVersion
    }
}

dependencyResolutionManagement {
    val kspVersion: String by settings
    val kotlinpoetVersion: String by settings

    repositories {
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
include("api")
include("impl")
include("ksp")
include("kts")
include("sample")
