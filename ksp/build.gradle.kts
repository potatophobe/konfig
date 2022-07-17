val projectRevision: String by project

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "ru.potatophobe.konfig"
version = projectRevision

dependencies {
    api(project(":api"))

    implementation(libs.ksp.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
}

publishing {
    publications {
        create<MavenPublication>("maven") { from(components["java"]) }
    }
}
