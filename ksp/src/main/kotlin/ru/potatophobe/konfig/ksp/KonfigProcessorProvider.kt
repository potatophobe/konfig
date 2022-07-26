package ru.potatophobe.konfig.ksp

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class KonfigProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): KonfigProcessor {
        return KonfigProcessor(environment.codeGenerator)
    }
}
