package ru.potatophobe.konfig

import kotlin.reflect.KClass

/**
 * Marks Konfig DSL components
 * */
@DslMarker
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
annotation class KonfigDsl

/**
 * Marks class as Konfig. Konfig DSL will be generated for it
 *
 * ```
 *
 *  data class Properties(
 *      val prop1: String,
 *      val prop2: String
 *  )
 *
 *  @KonfigClass("application")
 *  data class ApplicationKonfig(
 *      val name: String,
 *      @NestedKonfig
 *      val properties: Properties
 *  )
 * ```
 * will result
 * ```
 *  // KonfigScope
 *  application {
 *      name = ...
 *      properties = ...
 *  }
 *
 * ```
 *
 * @param name Konfig DSL root function name. Class name will be used by default
 * @param constructorBinding bind properties through single allArgs constructor
 * */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class KonfigClass(val name: String = "", val constructorBinding: Boolean = true)

/**
 * Marks property as Konfig. Konfig DSL will be generated for it
 *
 * ```
 *
 *  data class Properties(
 *      val prop1: String,
 *      val prop2: String
 *  )
 *
 *  @KonfigClass("application")
 *  data class ApplicationKonfig(
 *      val name: String,
 *      @NestedKonfig
 *      val properties: Properties
 *  )
 * ```
 * will result
 * ```
 *  // KonfigScope
 *  application {
 *      name = ...
 *      properties {
 *          prop1 = ...
 *          prop2 = ...
 *      }
 *  }
 *
 * ```
 *
 * @param name Konfig DSL root function name. Property name will be used by default
 * @param constructorBinding bind properties through single allArgs constructor
 * */
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY)
annotation class NestedKonfig(val name: String = "", val constructorBinding: Boolean = true)

/**
 * Provides access to defined Konfigs
 * */
interface Konfig {
    fun <T : Any> get(kClass: KClass<T>): T
    fun <T : Any> getOrNull(kClass: KClass<T>): T?
}

/**
 * Implicit receiver for all generated Konfig DSL root functions
 * */
@KonfigDsl
interface KonfigScope {
    fun add(konfig: Any)
}
