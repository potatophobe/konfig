plugins {
    kotlin("jvm")
}

group = "ru.potatophobe.konfig"

dependencies {
    implementation(project(":api"))

    implementation(kotlin("reflect"))
    implementation(libs.ksp.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
}