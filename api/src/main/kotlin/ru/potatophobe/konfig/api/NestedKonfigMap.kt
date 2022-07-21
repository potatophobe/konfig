package ru.potatophobe.konfig.api

/**
 * Marks property as map with nested Konfigs as values. Nested Konfig DSL will be generated for it
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
 *      @NestedKonfigMap
 *      val propertiesMap: Map<String, Properties>
 *  )
 * ```
 * will result
 * ```
 *  // KonfigScope
 *
 *  application {
 *      name = "sampleName"
 *      propertiesMap {
 *          key "sampleKey1" value {
 *              property1 = "sampleValue1"
 *              property2 = "sampleValue2"
 *          }
 *          key "sampleKey2" value {
 *              property1 = "sampleValue1"
 *              property2 = "sampleValue2"
 *          }
 *          key "sampleKey3" value {
 *              property1 = "sampleValue1"
 *              property2 = "sampleValue2"
 *          }
 *      }
 *  }
 *
 * ```
 *
 * @see KonfigClass
 * @see NestedKonfigList
 * @see NestedKonfigMap
 * */
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY)
annotation class NestedKonfigMap
