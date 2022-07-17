plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

group = "ru.potatophobe.konfig"

dependencies {
    implementation(project(":kts"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
