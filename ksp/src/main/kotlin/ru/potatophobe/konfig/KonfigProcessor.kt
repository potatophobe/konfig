package ru.potatophobe.konfig

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

class KonfigProcessor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(KONFIGCLASS_ANNOTATION_NAME).apply {
            if (iterator().hasNext()) {
                val konfigClassVisitor = KonfigClassVisitor(logger, codeGenerator)
                forEach { it.accept(konfigClassVisitor, Unit) }
            }
        }
        return emptyList()
    }
}