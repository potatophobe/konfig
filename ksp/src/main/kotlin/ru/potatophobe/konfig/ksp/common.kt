package ru.potatophobe.konfig.ksp

import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.symbol.KSClassDeclaration

internal const val SCOPE_SUFFIX = "Scope"
internal const val GENERATED_PACKAGE = "ru.potatophobe.konfig.generated"
internal const val UTILS_PACKAGE = "ru.potatophobe.konfig.utils"
internal const val LATEINIT_TYPE = "lateinit"
internal const val ISINITIALIZED_FUNCTION = "ru.potatophobe.konfig.utils.isInitialized"
internal const val TOKONFIG_FUNCTION = "toKonfig"
internal const val KONFIGBLOCK_PARAMETER = "konfigBlock"

internal fun validateKonfig(name: String, declaration: KSClassDeclaration) {
    if (!name.matches(Regex("[a-zA-Z_]\\w*"))) {
        throw IllegalArgumentException("Konfig name must match [a-zA-Z_]\\w* [$declaration]")
    }
    if (declaration.getConstructors().count() != 1) {
        throw IllegalArgumentException("Konfig class must have single allArgs constructor [$declaration]")
    }
}
