package ru.potatophobe.konfig.impl

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNull

class KonfigImplTest {
    class SampleKonfig

    class AnotherSampleKonfig

    class SampleNotKonfig

    @Test
    fun getTest() {
        val sampleKonfig = SampleKonfig()
        val anotherSampleKonfig = AnotherSampleKonfig()
        val konfigSet = KonfigSet().apply {
            add(sampleKonfig)
            add(anotherSampleKonfig)
        }

        val konfigImpl = KonfigImpl(konfigSet)

        assertEquals(sampleKonfig, konfigImpl.get(SampleKonfig::class))
        assertEquals(anotherSampleKonfig, konfigImpl.get(AnotherSampleKonfig::class))
        assertThrows<NoSuchElementException> { konfigImpl.get(SampleNotKonfig::class) }
    }

    @Test
    fun getOrNullTest() {
        val sampleKonfig = SampleKonfig()
        val anotherSampleKonfig = AnotherSampleKonfig()
        val konfigSet = KonfigSet().apply {
            add(sampleKonfig)
            add(anotherSampleKonfig)
        }

        val konfigImpl = KonfigImpl(konfigSet)

        assertEquals(sampleKonfig, konfigImpl.getOrNull(SampleKonfig::class))
        assertEquals(anotherSampleKonfig, konfigImpl.getOrNull(AnotherSampleKonfig::class))
        assertNull(konfigImpl.getOrNull(SampleNotKonfig::class))
    }
}
