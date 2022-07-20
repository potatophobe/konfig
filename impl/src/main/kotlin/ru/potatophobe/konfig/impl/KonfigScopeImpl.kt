package ru.potatophobe.konfig.impl

import ru.potatophobe.konfig.api.Konfig
import ru.potatophobe.konfig.api.KonfigScope

internal class KonfigScopeImpl : KonfigScope {
    private val konfigSet = KonfigSet()

    override fun add(konfig: Any) {
        konfigSet.add(konfig)
    }

    override fun toKonfig(): Konfig {
        return KonfigImpl(konfigSet)
    }
}
