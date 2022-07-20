package ru.potatophobe.konfig.impl

import org.junit.jupiter.api.Test
import kotlin.test.assertSame

class KonfigScopeImplTest {
    class SampleKonfig

    class AnotherSampleKonfig

    @Test
    fun toKonfigTest() {
        val konfigScopeImpl = KonfigScopeImpl()

        val firstSampleKonfig = SampleKonfig()
        val firstAnotherKonfig = AnotherSampleKonfig()

        konfigScopeImpl.add(firstSampleKonfig)
        konfigScopeImpl.add(firstAnotherKonfig)

        val firstKonfig = konfigScopeImpl.toKonfig()

        assertSame(firstSampleKonfig, firstKonfig.get(SampleKonfig::class))
        assertSame(firstAnotherKonfig, firstKonfig.get(AnotherSampleKonfig::class))

        val secondSampleKonfig = SampleKonfig()
        val secondAnotherKonfig = AnotherSampleKonfig()

        konfigScopeImpl.add(secondSampleKonfig)
        konfigScopeImpl.add(secondAnotherKonfig)

        val secondKonfig = konfigScopeImpl.toKonfig()

        assertSame(secondSampleKonfig, secondKonfig.get(SampleKonfig::class))
        assertSame(secondAnotherKonfig, secondKonfig.get(AnotherSampleKonfig::class))
    }
}