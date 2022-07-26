val projectRevision: String by project

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

group = "ru.potatophobe.konfig"
version = projectRevision

dependencies {
    ksp(project(":ksp"))
    implementation(project(":full"))
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}
