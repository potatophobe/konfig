val projectRevision: String by project

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "ru.potatophobe.konfig"
version = projectRevision

dependencies {
    api(project(":api"))
}

publishing {
    publications {
        create<MavenPublication>("maven") { from(components["java"]) }
    }
}
