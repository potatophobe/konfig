package ru.potatophobe.konfig

import ru.potatophobe.konfig.api.KonfigClass
import ru.potatophobe.konfig.api.NestedKonfig
import ru.potatophobe.konfig.api.NestedKonfigList
import ru.potatophobe.konfig.api.NestedKonfigMap

data class Properties(
    val prop1: String,
    val prop2: String
)

@KonfigClass("application")
data class ApplicationKonfig(
    val name: String = "application",
    @NestedKonfig
    val property: Property,
    @NestedKonfigList
    val properties: List<Property>,
    @NestedKonfigMap
    val propertiesMap: Map<String, Property>
) {
    data class Property(
        val value: String?
    )
}

fun main() {
//    println(KonfigKtsFactory().load().get(ApplicationKonfig::class))
}
