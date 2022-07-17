package ru.potatophobe.konfig

import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName

fun String.titleFirstChar(): String {
    return replaceFirstChar { it.titlecase() }
}

fun String.lowerFirstChar(): String {
    return replaceFirstChar { it.lowercase() }
}

internal fun KSAnnotated.getAnnotation(name: String): KSAnnotation {
    return annotations.single { it.annotationType.resolve().declaration.qualifiedName?.asString() == name }
}

internal fun KSAnnotated.getAnnotationOrNull(name: String): KSAnnotation? {
    return annotations.singleOrNull { it.annotationType.resolve().declaration.qualifiedName?.asString() == name }
}

internal inline fun <reified T> KSAnnotation.getArgument(name: String): T {
    return arguments.single { it.name?.asString() == name && it.value is T }.value as T
}

internal fun KSClassDeclaration.getSingleConstructorParameters(): List<KSValueParameter> {
    return getConstructors().single().parameters
}

internal fun KSClassDeclaration.getPublicMutableProperties(): Sequence<KSPropertyDeclaration> {
    return getAllProperties().filter { it.modifiers.contains(Modifier.PUBLIC) && it.isMutable }
}

internal fun KSValueParameter.nameAsString(): String {
    return name?.asString() ?: throw IllegalArgumentException("Value parameter $this has no name")
}

internal fun lambdaTypeName(
    receiver: TypeName? = null,
    parameters: List<ParameterSpec> = emptyList(),
    returnType: TypeName = Unit::class.asClassName()
): LambdaTypeName {
    return LambdaTypeName.get(receiver, parameters, returnType)
}
