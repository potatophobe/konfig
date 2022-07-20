package ru.potatophobe.konfig.impl

internal class KonfigSet private constructor(private val delegate: MutableSet<Any>) : MutableSet<Any> by delegate {
    constructor() : this(mutableSetOf())

    override fun add(element: Any): Boolean {
        singleOrNull { it::class == element::class }?.let { remove(it) }
        return delegate.add(element)
    }
}
