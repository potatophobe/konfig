package ru.potatophobe.konfig

import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toTypeName
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

internal const val KONFIGCLASS_ANNOTATION_NAME = "ru.potatophobe.konfig.KonfigClass"

internal const val NESTEDKONFIG_ANNOTATION_NAME = "ru.potatophobe.konfig.NestedKonfig"

internal const val NULL = "null"

internal const val TOKONFIG_FUN_NAME = "toKonfig"

internal const val KONFIGBLOCK_PARAMETER_NAME = "konfigBlock"

@OptIn(ExperimentalContracts::class)
internal fun validateKonfig(konfigName: String, constructorBinding: Boolean, declaration: KSDeclaration) {
    contract {
        returns() implies (declaration is KSClassDeclaration)
    }
    if (!konfigName.matches(Regex("[a-zA-Z_]\\w*"))) {
        throw IllegalArgumentException("Konfig name must match [a-zA-Z_]\\w* [$declaration]")
    }
    if (declaration !is KSClassDeclaration) {
        throw IllegalArgumentException("Konfig must be a class [$declaration]")
    }
    if (constructorBinding && declaration.getConstructors().count() != 1) {
        throw IllegalArgumentException("Konfig class must have single allArgs constructor for constructor binding [$declaration]")
    }
    if (!constructorBinding && declaration.getConstructors().singleOrNull { it.parameters.isEmpty() } == null) {
        throw IllegalArgumentException("Konfig class must have noArgs constructor for property binding [$declaration]")
    }
}

internal fun TypeSpec.Builder.configureKonfigScope(parameters: List<KSValueParameter>) = apply {
    addAnnotation(KonfigDsl::class)
    parameters.forEach {
        addKonfigProperty(it.nameAsString(), it.type.toTypeName())
    }
}

internal fun TypeSpec.Builder.configureKonfigScope(properties: Sequence<KSPropertyDeclaration>) = apply {
    addAnnotation(KonfigDsl::class)
    properties.forEach {
        addKonfigProperty(it.simpleName.asString(), it.type.toTypeName())
    }
}

internal fun FunSpec.Builder.configureToKonfig(
    konfigScopeClassName: ClassName,
    className: ClassName,
    parameters: List<KSValueParameter>
) = apply {
    addModifiers(KModifier.PRIVATE)
    receiver(konfigScopeClassName)
    returns(className)
    addToKonfigCode(className, parameters.map { it.nameAsString() })
}

internal fun FunSpec.Builder.configureToKonfig(
    konfigScopeClassName: ClassName,
    className: ClassName,
    properties: Sequence<KSPropertyDeclaration>
) = apply {
    addModifiers(KModifier.PRIVATE)
    receiver(konfigScopeClassName)
    returns(className)
    addToKonfigCode(className, properties.map { it.simpleName.asString() }.toList())
}

internal fun TypeSpec.Builder.addKonfigProperty(
    name: String,
    typeName: TypeName
) = apply {
    addProperty(
        PropertySpec.builder(name, typeName.copy(true)).apply {
            addAnnotation(KonfigDsl::class)
            mutable(true)
            initializer(NULL)
        }.build()
    )
}

internal fun FunSpec.Builder.addToKonfigCode(className: ClassName, parameterNames: List<String>) = apply {
    addCode(
        """
return ${className.simpleName}::class.constructors.single().call(${parameterNames.joinToString(",")})
        """.trimIndent()
    )
}