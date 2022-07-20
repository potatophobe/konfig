package ru.potatophobe.konfig.utils

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class LateinitTest {
    class Sample {
        var property: String by lateinit()
    }

    @Test
    fun lateinitTest() {
        val sample = Sample()

        assertFalse { isInitialized(sample::property) }
        assertThrows<UninitializedPropertyAccessException> { sample.property }

        val value = "value"
        sample.property = value

        assertTrue { isInitialized(sample::property) }
        assertDoesNotThrow { sample.property }
        assertSame(value, sample.property)
    }
}