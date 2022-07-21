package ru.potatophobe.konfig.api

import ru.potatophobe.konfig.utils.lateinit

/**
 * Utility scope for generated scopes to construct map with Konfigs as values
 * */
@KonfigDsl
open class KonfigMapScope<K, VS> {
    private val mutableKeysToKonfigMapValueDescriptors: MutableMap<K, KonfigMapValueDescriptor<VS>> = mutableMapOf()
    val keysToKonfigBlocks: Map<K, VS.() -> Unit> = mutableKeysToKonfigMapValueDescriptors.mapValues { (_, v) -> v.konfigBlock }

    @KonfigDsl
    open fun key(key: K): KonfigMapValueDescriptor<VS> {
        return KonfigMapValueDescriptor<VS>().also { mutableKeysToKonfigMapValueDescriptors[key] = it }
    }

    @KonfigDsl
    open class KonfigMapValueDescriptor<VS> {
        var konfigBlock: VS.() -> Unit by lateinit()
            private set

        @KonfigDsl
        infix fun value(konfigBlock: VS.() -> Unit) {
            this.konfigBlock = konfigBlock
        }
    }
}
