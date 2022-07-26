package ru.potatophobe.konfig.impl

import org.junit.jupiter.api.Test
import kotlin.test.assertSame

class KonfigSetTest {
    class SampleKonfig

    class AnotherSampleKonfig

    @Test
    fun addTest() {
        val konfigSet = KonfigSet()

        val firstSampleKonfig = SampleKonfig()
        val firstAnotherKonfig = AnotherSampleKonfig()

        konfigSet.add(firstSampleKonfig)
        konfigSet.add(firstAnotherKonfig)

        assertSame(firstSampleKonfig, konfigSet.single { it::class == SampleKonfig::class })
        assertSame(firstAnotherKonfig, konfigSet.single { it::class == AnotherSampleKonfig::class })

        val secondSampleKonfig = SampleKonfig()
        val secondAnotherKonfig = AnotherSampleKonfig()

        konfigSet.add(secondSampleKonfig)
        konfigSet.add(secondAnotherKonfig)

        assertSame(secondSampleKonfig, konfigSet.single { it::class == SampleKonfig::class })
        assertSame(secondAnotherKonfig, konfigSet.single { it::class == AnotherSampleKonfig::class })
    }
}