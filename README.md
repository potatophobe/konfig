# `Konfig`

### Kotlin DSL config

## Modules

- `api` - API annotations and `Konfig` container interface
- `impl` - simple API implementation and default `konfig {...}` DSL entry point
- `ksp` - Kotlin Symbol Processor generating DSL according to API annotations
- `kts` - Kotlin Script definition allowing to describe Konfig inside `*.konfig.kts` file

## Quick start

### With Kotlin Script

```kotlin
//build.gradle.kts
plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") // Plugin to run Kotlin Symbol Processor
}

dependencies {
    ksp("ru.potatophobe.konfig:ksp:$konfigVersion") // Kotlin Symbol Processor
    implementation("ru.potatophobe.konfig:kts:$konfigVersion") // Kotlin Script Konfig implementation
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin") // There will be files generated by Kotlin Symbol Processor
    }
}
```

```kotlin
//ApplicationKonfig.kt
data class Properties(
    val prop1: String,
    val prop2: String
)

@KonfigClass("application")
data class ApplicationKonfig(
    val name: String,
    @NestedKonfig
    val properties: Properties
)
```

`>> gradle build`

```kotlin
//application.konfig.kts
application {
    name = "sample"
    properties {
        prop1 = "1"
        prop2 = "2"
    }
}
```

```kotlin
//main.kt
val konfigKtsFactory = KonfigKtsFactory() // Factory to load Konfig from Kotlin Script file

val konfig = konfigKtsFactory.load() // By default, 'resources/application.konfig.kts' will be loaded

val applicationKonfig = konfig.get(ApplicationKonfig::class) // ApplicationKonfig(name=sample, properties=Properties(prop1=1, prop2=2))
```

### With plain Kotlin

```kotlin
//build.gradle.kts same as above
dependencies {
    ksp("ru.potatophobe.konfig:ksp:$konfigVersion")
    implementation("ru.potatophobe.konfig:impl:$konfigVersion") // Plain Kotlin Konfig implementation
}
```

```kotlin
//main.kt
val konfig = konfig {
    application {
        name = "sample"
        properties {
            prop1 = "1"
            prop2 = "2"
        }
    }
}

val applicationKonfig = konfig.get(ApplicationKonfig::class) // ApplicationKonfig(name=sample, properties=Properties(prop1=1, prop2=2))
```
