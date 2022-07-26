package ru.potatophobe.konfig.utils

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

/**
 * Delegate for late initialization any mutable properties, not only non-nullable like with `lateinit` modifier.
 * Note that it is impossible to disable ability to delegate immutable properties to this, so it's up to you.
 * If you delegate immutable property to this, you will never initialize it
 *
 * @see lateinit
 * */
open class Lateinit<T> : ReadWriteProperty<Any?, T> {
    /**
     * Use it to check if property is initialized
     * */
    var isInitialized: Boolean = false
        private set

    @Volatile
    private var value: T? = null
        set(value) {
            isInitialized = true
            field = value
        }

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (!isInitialized) throw UninitializedPropertyAccessException("Lateinit property ${property.name} has not been initialized")
        return value as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        synchronized(this) {
            this.value = value
        }
    }
}

/**
 * Use it to check if property is initialized
 * */
fun isInitialized(property: KProperty0<*>): Boolean {
    property.isAccessible = true
    val delegate = property.getDelegate()
    property.isAccessible = false

    return if (delegate is Lateinit<*>) delegate.isInitialized
    else true
}

/**
 * Simple alias for more beautiful usage
 *
 * ```
 *
 *  class SomeClass {
 *      var someProperty: String? by lateinit()
 *  }
 *
 * ```
 * */
typealias lateinit<T> = Lateinit<T>
