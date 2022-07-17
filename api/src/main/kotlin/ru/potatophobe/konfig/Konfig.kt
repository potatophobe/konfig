package ru.potatophobe.konfig

import kotlin.reflect.KClass

@DslMarker
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
annotation class KonfigDsl

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class KonfigClass(val name: String = "", val constructorBinding: Boolean = true)

@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY)
annotation class NestedKonfig(val name: String = "", val constructorBinding: Boolean = true)

interface Konfig {
    fun <T : Any> get(kClass: KClass<T>): T
    fun <T : Any> getOrNull(kClass: KClass<T>): T?
}

@KonfigDsl
interface KonfigScope {
    fun add(konfig: Any)
}
