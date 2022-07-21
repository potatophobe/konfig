package ru.potatophobe.konfig.api

/**
 * Utility scope for generated scopes to construct list of Konfigs
 * */
@KonfigDsl
open class KonfigListScope<ES> {
    private val mutableKonfigBlocks: MutableList<ES.() -> Unit> = mutableListOf()
    open val konfigBlocks: List<ES.() -> Unit> get() = mutableKonfigBlocks.toList()

    @KonfigDsl
    open fun element(konfigBlock: ES.() -> Unit) {
        mutableKonfigBlocks.add(konfigBlock)
    }
}
