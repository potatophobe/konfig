plugins {
    kotlin("jvm")
}

group = "ru.potatophobe.konfig"

dependencies {
    api(project(":api"))
    implementation(project(":impl"))

    api(kotlin("scripting-jvm"))
    implementation(kotlin("compiler-embeddable"))
    implementation(kotlin("scripting-jvm-host"))
}
