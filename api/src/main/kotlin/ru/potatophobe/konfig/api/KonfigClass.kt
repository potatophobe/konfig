package ru.potatophobe.konfig.api

/**
 * Marks class as Konfig. Konfig DSL will be generated for it
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
 * @param name Konfig DSL root function name. Class name will be used by default
 * @param constructorBinding bind properties through single allArgs constructor
 *
 * @see KonfigScope
 * @see NestedKonfig
 * @see NestedKonfigList
 * @see NestedKonfigMap
 * */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class KonfigClass(val name: String = "", val constructorBinding: Boolean = true)
