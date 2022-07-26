package ru.potatophobe.konfig

import ru.potatophobe.konfig.api.KonfigClass
import ru.potatophobe.konfig.api.NestedKonfig
import ru.potatophobe.konfig.api.NestedKonfigList
import ru.potatophobe.konfig.api.NestedKonfigMap
import ru.potatophobe.konfig.kts.KonfigKtsFactory

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
    val konfig = KonfigKtsFactory().load()

    val applicationKonfig = konfig.get(ApplicationKonfig::class)

    println(applicationKonfig)
}
