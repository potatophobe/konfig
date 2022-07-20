package ru.potatophobe.konfig.api

/**
 * Marks property as nested Konfig. Nested Konfig DSL will be generated for it
 *
 * ```
 *
 *  data class Properties(
 *      val property1: String,
 *      val property2: String
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
 *
 *  application {
 *      name = "sampleName"
 *      properties {
 *          property1 = "sampleValue1"
 *          property2 = "sampleValue2"
 *      }
 *  }
 *
 * ```
 *
 * @param name nested Konfig DSL function name. Property name will be used by default
 * @param constructorBinding bind properties through single allArgs constructor
 *
 * @see KonfigClass
 * @see NestedKonfigList
 * @see NestedKonfigMap
 * */
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY)
annotation class NestedKonfig(val name: String = "", val constructorBinding: Boolean = true)
