package ru.potatophobe.konfig

import kotlin.reflect.KClass

private class KonfigSet private constructor(private val delegate: MutableSet<Any>) : MutableSet<Any> by delegate {
    constructor() : this(mutableSetOf())

    override fun add(element: Any): Boolean {
        if (singleOrNull { it::class == element::class } != null) {
            throw IllegalArgumentException("KonfigSet already contains ${element::class}")
        }
        return delegate.add(element)
    }
}

private class KonfigImpl(
    private val konfigSet: KonfigSet
) : Konfig {

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(kClass: KClass<T>): T {
        return konfigSet.single { it::class == kClass } as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getOrNull(kClass: KClass<T>): T? {
        return konfigSet.singleOrNull { it::class == kClass } as T?
    }
}

private class KonfigScopeImpl : KonfigScope {
    private val konfigSet = KonfigSet()

    override fun add(konfig: Any) {
        konfigSet.add(konfig)
    }

    fun toKonfig(): Konfig {
        return KonfigImpl(konfigSet)
    }
}

@KonfigDsl
fun konfig(konfigBlock: KonfigScope.() -> Unit): Konfig {
    return KonfigScopeImpl().apply(konfigBlock).toKonfig()
}
