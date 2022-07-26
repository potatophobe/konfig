package ru.potatophobe.konfig.utils

import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.symbol.*

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

internal fun KSDeclaration.asClassDeclaration(): KSClassDeclaration {
    return this as KSClassDeclaration
}

internal fun KSClassDeclaration.getSingleConstructorParameters(): List<KSValueParameter> {
    return getConstructors().single().parameters
}

internal fun KSValueParameter.nameAsString(): String {
    return name?.asString() ?: throw IllegalArgumentException("Value parameter $this has no name")
}
