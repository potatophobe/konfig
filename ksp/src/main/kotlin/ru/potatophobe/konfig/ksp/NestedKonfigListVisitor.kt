package ru.potatophobe.konfig.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import ru.potatophobe.konfig.api.*
import ru.potatophobe.konfig.utils.*
import kotlin.reflect.KParameter

class NestedKonfigListVisitor(
    private val codeGenerator: CodeGenerator,
    private val generatedKonfigScopes: MutableMap<KSClassDeclaration, ClassName>,
    private val outerKonfigName: String,
    private val outerKonfigScope: ClassName
) : KSEmptyVisitor<Unit, Unit>() {

    override fun defaultHandler(node: KSNode, data: Unit) {
        throw UnsupportedOperationException("NestedKonfigList must be a parameter [$node]")
    }

    override fun visitValueParameter(valueParameter: KSValueParameter, data: Unit) {
        val name = valueParameter.nameAsString()
        val type = valueParameter.type.resolve()
        val classDeclaration = type.declaration.asClassDeclaration()
        if (classDeclaration.toClassName().canonicalName != LIST.canonicalName) {
            throw IllegalArgumentException("NestedKonfigList must be a list [$valueParameter]")
        }
        val konfigClassDeclaration = type.arguments.single().type!!.resolve().declaration.asClassDeclaration()
        validateKonfig(name, konfigClassDeclaration)
        generateKonfig(name, konfigClassDeclaration)
    }

    private fun generateKonfig(name: String, classDeclaration: KSClassDeclaration) {
        FileSpec.builder(GENERATED_PACKAGE, "${outerKonfigName.titleFirstChar()}${name.titleFirstChar()}").apply {
            val scopeClassName = generatedKonfigScopes.getOrPut(classDeclaration) {
                ClassName(GENERATED_PACKAGE, "${classDeclaration.toClassName().simpleNames.joinToString("")}$SCOPE_SUFFIX").also { className ->
                    val parameters = classDeclaration.getSingleConstructorParameters()
                    addImport(UTILS_PACKAGE, LATEINIT_TYPE)
                    addImport(UTILS_PACKAGE, ISINITIALIZED_FUNCTION)
                    addType(
                        TypeSpec.classBuilder(className).apply {
                            addAnnotation(KonfigDsl::class)
                            parameters.forEach {
                                addProperty(
                                    PropertySpec.builder(it.nameAsString(), it.type.toTypeName()).apply {
                                        addAnnotation(KonfigDsl::class.asClassName())
                                        mutable(true)
                                        delegate("$LATEINIT_TYPE()")
                                    }.build()
                                )
                            }
                            addFunction(
                                FunSpec.builder(TOKONFIG_FUNCTION).apply {
                                    returns(classDeclaration.toClassName())
                                    addCode("val constructor = ${classDeclaration.toClassName().simpleNames.joinToString(".")}::class.constructors.single()\n")
                                    addCode("val parameterMap = mutableMapOf<${KParameter::class.asClassName().canonicalName}, Any?>()\n")
                                    addCode(
                                        parameters.joinToString("") {
                                            "if ($ISINITIALIZED_FUNCTION(this::${it.nameAsString()}))\n" +
                                                    "\tparameterMap[constructor.parameters.single { it.name == \"${it.nameAsString()}\" }] = this.${it.nameAsString()}\n"
                                        }
                                    )
                                    addCode("return constructor.callBy(parameterMap)")
                                }.build()
                            )
                        }.build()
                    )
                    val nestedKonfigs = parameters.filter { it.getAnnotationOrNull(NestedKonfig::class.asClassName().canonicalName) != null }
                    if (nestedKonfigs.isNotEmpty()) {
                        val nestedKonfigVisitor = NestedKonfigVisitor(codeGenerator, generatedKonfigScopes, name, className)
                        nestedKonfigs.forEach {
                            it.accept(nestedKonfigVisitor, Unit)
                        }
                    }
                    val nestedKonfigLists = parameters.filter { it.getAnnotationOrNull(NestedKonfigList::class.asClassName().canonicalName) != null }
                    if (nestedKonfigLists.isNotEmpty()) {
                        val nestedKonfigListVisitor = NestedKonfigListVisitor(codeGenerator, generatedKonfigScopes, name, className)
                        nestedKonfigLists.forEach {
                            it.accept(nestedKonfigListVisitor, Unit)
                        }
                    }
                    val nestedKonfigMaps = parameters.filter { it.getAnnotationOrNull(NestedKonfigMap::class.asClassName().canonicalName) != null }
                    if (nestedKonfigMaps.isNotEmpty()) {
                        val nestedKonfigMapVisitor = NestedKonfigMapVisitor(codeGenerator, generatedKonfigScopes, name, className)
                        nestedKonfigMaps.forEach {
                            it.accept(nestedKonfigMapVisitor, Unit)
                        }
                    }
                }
            }
            addFunction(
                FunSpec.builder(name).apply {
                    addAnnotation(KonfigDsl::class.asClassName())
                    receiver(outerKonfigScope)
                    addParameter(
                        ParameterSpec(
                            KONFIGBLOCK_PARAMETER,
                            LambdaTypeName.get(KonfigListScope::class.asClassName().parameterizedBy(scopeClassName), emptyList(), UNIT)
                        )
                    )
                    addCode(
                        "this.$name = ${KonfigListScope::class.asClassName().simpleNames.joinToString(".")}<${
                            scopeClassName.simpleNames.joinToString(".")
                        }>().apply($KONFIGBLOCK_PARAMETER)\n"
                    )
                    addCode(".konfigBlocks.map { ${scopeClassName.simpleNames.joinToString(".")}().apply(it).$TOKONFIG_FUNCTION() }")
                }.build()
            )
        }.build().writeTo(codeGenerator, false)
    }
}