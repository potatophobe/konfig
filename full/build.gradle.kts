val projectRevision: String by project

plugins {
    id("java-platform")
    id("maven-publish")
    id("signing")
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
            from(components["javaPlatform"])
            pom {
                name.set("Konfig Full")
                description.set("Full set of Konfig modules")
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
