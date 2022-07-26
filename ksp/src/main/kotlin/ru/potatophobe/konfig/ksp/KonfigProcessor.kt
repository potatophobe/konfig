package ru.potatophobe.konfig.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.asClassName
import ru.potatophobe.konfig.api.KonfigClass
import ru.potatophobe.konfig.utils.getAnnotation
import ru.potatophobe.konfig.utils.getArgument

class KonfigProcessor(
    private val codeGenerator: CodeGenerator
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(KonfigClass::class.asClassName().canonicalName).apply {
            if (iterator().hasNext()) {
                val konfigClassVisitor = KonfigClassVisitor(codeGenerator, mutableMapOf())
                forEach { it.accept(konfigClassVisitor, it.getAnnotation(KonfigClass::class.asClassName().canonicalName).getArgument("name")) }
            }
        }
        return emptyList()
    }
}
