package ru.potatophobe.konfig

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class KonfigProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): KonfigProcessor {
        return KonfigProcessor(environment.logger, environment.codeGenerator)
    }
}