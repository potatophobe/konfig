package ru.potatophobe.konfig.impl

import ru.potatophobe.konfig.api.Konfig
import ru.potatophobe.konfig.api.KonfigDsl
import ru.potatophobe.konfig.api.KonfigScope
import kotlin.reflect.KClass

internal class KonfigImpl(private val konfigSet: KonfigSet) : Konfig {

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(kClass: KClass<T>): T {
        return konfigSet.single { it::class == kClass } as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getOrNull(kClass: KClass<T>): T? {
        return konfigSet.singleOrNull { it::class == kClass } as T?
    }
}

/**
 * Constructs KonfigScope and applies konfigBlock to it
 *
 * @return Konfig container with all declared inside konfigs
 *
 * @see Konfig
 * */
@KonfigDsl
fun konfig(konfigBlock: KonfigScope.() -> Unit): Konfig {
    return KonfigScopeImpl().apply(konfigBlock).toKonfig()
}
