package ru.potatophobe.konfig.api

/**
 * Implicit receiver for all generated Konfig DSL root functions
 *
 * @see KonfigClass
 * */
@KonfigDsl
interface KonfigScope {
    fun add(konfig: Any)
}
