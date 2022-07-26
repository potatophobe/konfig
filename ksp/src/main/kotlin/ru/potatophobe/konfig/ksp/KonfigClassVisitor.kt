package ru.potatophobe.konfig.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import ru.potatophobe.konfig.api.*
import ru.potatophobe.konfig.utils.*
import kotlin.reflect.KParameter

class KonfigClassVisitor(
    private val codeGenerator: CodeGenerator,
    private val generatedKonfigScopes: MutableMap<KSClassDeclaration, ClassName>
) : KSEmptyVisitor<String, Unit>() {

    override fun defaultHandler(node: KSNode, data: String) {
        throw UnsupportedOperationException("Konfig must be a class [$node]")
    }

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: String) {
        val name = data.ifEmpty { classDeclaration.simpleName.asString().lowerFirstChar() }

        validateKonfig(name, classDeclaration)
        generateKonfig(name, classDeclaration)
    }

    private fun generateKonfig(name: String, classDeclaration: KSClassDeclaration) {
        FileSpec.builder(GENERATED_PACKAGE, name.titleFirstChar()).apply {
            val scopeClassName = generatedKonfigScopes.getOrPut(classDeclaration) {
                ClassName(GENERATED_PACKAGE, "${classDeclaration.toClassName().simpleNames.joinToString("")}$SCOPE_SUFFIX").also { className ->
                    val parameters = classDeclaration.getSingleConstructorParameters()
                    addType(
                        TypeSpec.classBuilder(className).apply {
                            parameters.forEach {
                                addProperty(
                                    PropertySpec.builder(it.nameAsString(), it.type.toTypeName()).apply {
                                        addAnnotation(KonfigDsl::class.asClassName())
                                        mutable(true)
                                        delegate("${lateinit::class.asClassName().canonicalName}()")
                                    }.build()
                                )
                            }
                            addFunction(
                                FunSpec.builder(TOKONFIG_FUNCTION).apply {
                                    returns(classDeclaration.toClassName())
                                    addCode(
                                        """
                                        val constructor = ${classDeclaration.toClassName().simpleName}::class.constructors.single()
                                        val parameterMap = mutableMapOf<${KParameter::class.asClassName().canonicalName}, Any?>()
                                        ${
                                            parameters.joinToString("\n") {
                                                """
                                                if ($ISINITIALIZED_FUNCTION(this::${it.nameAsString()}))
                                                    parameterMap[constructor.parameters.single { it.name == "${it.nameAsString()}" }] = this.${it.nameAsString()}
                                                """.trimIndent()
                                            }
                                        }
                                        return constructor.callBy(parameterMap)
                                        """.trimIndent()
                                    )
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
                    receiver(KonfigScope::class.asClassName())
                    addParameter(ParameterSpec(KONFIGBLOCK_PARAMETER, LambdaTypeName.get(scopeClassName, emptyList(), UNIT)))
                    addCode(
                        """
                        add(${scopeClassName.simpleName}().apply($KONFIGBLOCK_PARAMETER).$TOKONFIG_FUNCTION())
                        """.trimIndent()
                    )
                }.build()
            )
        }.build().writeTo(codeGenerator, false)
    }
}