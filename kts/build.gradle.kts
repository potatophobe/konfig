val projectRevision: String by project

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "ru.potatophobe.konfig"
version = projectRevision

dependencies {
    api(project(":api"))
    implementation(project(":impl"))

    api(kotlin("scripting-jvm"))
    implementation(kotlin("compiler-embeddable"))
    implementation(kotlin("scripting-jvm-host"))
}

publishing {
    publications {
        create<MavenPublication>("maven") { from(components["java"]) }
    }
}
