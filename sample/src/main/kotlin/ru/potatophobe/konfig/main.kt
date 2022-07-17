package ru.potatophobe.konfig

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

fun main() {
    println(KonfigKtsFactory().load().get(ApplicationKonfig::class))
}
