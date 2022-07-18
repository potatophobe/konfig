val projectRevision: String by project

plugins {
    `java-platform`
    `maven-publish`
}

group = "ru.potatophobe.konfig"
version = projectRevision

javaPlatform {
    allowDependencies()
}

dependencies {
    api(project(":api"))
    api(project(":impl"))
    api(project(":kts"))
    api(project(":utils"))
}

publishing {
    publications {
        create<MavenPublication>("maven") { from(components["javaPlatform"]) }
    }
}