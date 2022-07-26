package ru.potatophobe.konfig.api

/**
 * Marks property as list of nested Konfigs. Nested Konfig DSL will be generated for it
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
 *      @NestedKonfigList
 *      val propertiesList: List<Properties>
 *  )
 * ```
 * will result
 * ```
 *  // KonfigScope
 *
 *  application {
 *      name = "sampleName"
 *      propertiesList {
 *          element {
 *              property1 = "sampleValue1"
 *              property2 = "sampleValue2"
 *          }
 *          element {
 *              property1 = "sampleValue1"
 *              property2 = "sampleValue2"
 *          }
 *          element {
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
annotation class NestedKonfigList
