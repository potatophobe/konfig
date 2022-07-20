package ru.potatophobe.konfig.api

import kotlin.reflect.KClass

/**
 * Container for declared Konfigs
 *
 * @see KonfigClass
 * */
interface Konfig {
    fun <T : Any> get(kClass: KClass<T>): T
    fun <T : Any> getOrNull(kClass: KClass<T>): T?
}
