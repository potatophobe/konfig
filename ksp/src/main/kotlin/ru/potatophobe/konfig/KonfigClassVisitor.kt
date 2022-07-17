package ru.potatophobe.konfig

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

class KonfigClassVisitor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator
) : KSEmptyVisitor<Unit, Unit>() {

    override fun defaultHandler(node: KSNode, data: Unit) {
        throw UnsupportedOperationException("$node can't be Konfig")
    }

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val annotation = classDeclaration.getAnnotation(KONFIGCLASS_ANNOTATION_NAME)
        val konfigName = annotation.getArgument<String>("name").ifEmpty { classDeclaration.simpleName.asString().lowerFirstChar() }
        val constructorBinding: Boolean = annotation.getArgument("constructorBinding")

        validateKonfig(konfigName, constructorBinding, classDeclaration)

        FileSpec.builder(classDeclaration.packageName.asString(), "${classDeclaration.simpleName.asString()}Generated")
            .addKonfig(konfigName, constructorBinding, classDeclaration)
            .build().writeTo(codeGenerator, false)
    }

    private fun FileSpec.Builder.addKonfig(
        konfigName: String,
        constructorBinding: Boolean,
        classDeclaration: KSClassDeclaration
    ) = apply {
        val className = classDeclaration.toClassName()
        val konfigScopeClassName = ClassName(packageName, "${konfigName.titleFirstChar()}Scope")
        addType(
            TypeSpec.classBuilder(konfigScopeClassName).apply {
                val nestedKonfigs = classDeclaration.let {
                    if (constructorBinding) it.getSingleConstructorParameters().apply { configureKonfigScope(this) }
                    else it.getPublicMutableProperties().apply { configureKonfigScope(this) }.toList()
                }.filter { it.getAnnotationOrNull(NESTEDKONFIG_ANNOTATION_NAME) != null }
                if (nestedKonfigs.isNotEmpty()) {
                    val nestedKonfigVisitor = NestedKonfigVisitor(logger, konfigScopeClassName, this)
                    nestedKonfigs.forEach { it.accept(nestedKonfigVisitor, Unit) }
                }
            }.build()
        )
        addFunction(
            FunSpec.builder(TOKONFIG_FUN_NAME).apply {
                if (constructorBinding) configureToKonfig(konfigScopeClassName, className, classDeclaration.getSingleConstructorParameters())
                else configureToKonfig(konfigScopeClassName, className, classDeclaration.getPublicMutableProperties())
            }.build()
        )
        addFunction(
            FunSpec.builder(konfigName).apply {
                addAnnotation(KonfigDsl::class)
                receiver(KonfigScope::class)
                addParameter(ParameterSpec(KONFIGBLOCK_PARAMETER_NAME, lambdaTypeName(konfigScopeClassName)))
                addCode(
                    """
            add(${konfigScopeClassName.simpleName}().apply(${KONFIGBLOCK_PARAMETER_NAME}).${TOKONFIG_FUN_NAME}())
                    """.trimIndent()
                )
            }.build()
        )
    }
}