package ru.potatophobe.konfig

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName

class NestedKonfigVisitor(
    private val logger: KSPLogger,
    private val outerKonfigScopeClassName: ClassName,
    private val typeSpecBuilder: TypeSpec.Builder
) : KSEmptyVisitor<Unit, Unit>() {

    override fun defaultHandler(node: KSNode, data: Unit) {
        throw UnsupportedOperationException("$node can't be NestedKonfig")
    }

    override fun visitValueParameter(valueParameter: KSValueParameter, data: Unit) {
        val annotation = valueParameter.getAnnotation(NESTEDKONFIG_ANNOTATION_NAME)
        val konfigName = annotation.getArgument<String>("name").ifEmpty { valueParameter.nameAsString() }
        val constructorBinding: Boolean = annotation.getArgument("constructorBinding")

        val declaration = valueParameter.type.resolve().declaration
        validateKonfig(konfigName, constructorBinding, declaration)

        typeSpecBuilder.addKonfig(konfigName, constructorBinding, valueParameter.nameAsString(), declaration)
    }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
        val annotation = property.getAnnotation(NESTEDKONFIG_ANNOTATION_NAME)
        val konfigName = annotation.getArgument<String>("name").ifEmpty { property.simpleName.asString() }
        val constructorBinding: Boolean = annotation.getArgument("constructorBinding")

        val declaration = property.type.resolve().declaration
        validateKonfig(konfigName, constructorBinding, declaration)

        typeSpecBuilder.addKonfig(konfigName, constructorBinding, property.simpleName.asString(), declaration)
    }

    private fun TypeSpec.Builder.addKonfig(
        konfigName: String,
        constructorBinding: Boolean,
        propertyName: String,
        classDeclaration: KSClassDeclaration
    ) = apply {
        val className = classDeclaration.toClassName()
        val konfigScopeClassName = ClassName(
            outerKonfigScopeClassName.packageName,
            *outerKonfigScopeClassName.simpleNames.toTypedArray(),
            "${konfigName.titleFirstChar()}Scope"
        )
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
                addParameter(ParameterSpec(KONFIGBLOCK_PARAMETER_NAME, lambdaTypeName(konfigScopeClassName)))
                addCode(
                    """
            $propertyName = ${konfigScopeClassName.simpleName}().apply(${KONFIGBLOCK_PARAMETER_NAME}).${TOKONFIG_FUN_NAME}()
                    """.trimIndent()
                )
            }.build()
        )
    }
}