val projectRevision: String by project

plugins {
    kotlin("jvm")
    id("maven-publish")
    id("signing")
}

group = "ru.potatophobe.konfig"
version = projectRevision

dependencies {
    api(kotlin("reflect"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            val ossrhUsername: String by project
            val ossrhPassword: String by project

            url = run {
                if (projectRevision.endsWith("SNAPSHOT")) uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                else uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            }
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set("Konfig API")
                description.set("API for type-safe DSL Kotlin configuring")
                url.set("https://github.com/potatophobe/konfig")
                developers {
                    developer {
                        id.set("potatophobe")
                        name.set("Artem Stroev")
                        email.set("potatophobe@gmail.com")
                    }
                }
                licenses {
                    license {
                        name.set("Apache License Version 2.0")
                        url.set("https://raw.githubusercontent.com/potatophobe/konfig/master/LICENSE")
                    }
                }
                scm {
                    url.set("https://github.com/potatophobe/konfig")
                    connection.set("scm:git:https://github.com/potatophobe/konfig.git")
                    developerConnection.set("scm:git:https://github.com/potatophobe/konfig.git")
                }
            }

        }
    }
}

signing {
    sign(publishing.publications["maven"])
}
