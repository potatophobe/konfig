package ru.potatophobe.konfig.kts

import org.jetbrains.kotlin.cli.common.repl.KotlinJsr223JvmScriptEngineFactoryBase
import org.jetbrains.kotlin.cli.common.repl.ScriptArgsWithTypes
import ru.potatophobe.konfig.api.Konfig
import javax.script.ScriptEngine
import kotlin.script.experimental.jvmhost.createJvmScriptDefinitionFromTemplate
import kotlin.script.experimental.jvmhost.jsr223.KotlinJsr223ScriptEngineImpl

/**
 * Factory creates engine to evaluate `*.konfig.kts`
 *
 * @see KonfigKtsScript
 * */
class KonfigKtsScriptEngineFactory : KotlinJsr223JvmScriptEngineFactoryBase() {
    private val scriptDefinition = createJvmScriptDefinitionFromTemplate<KonfigKtsScript>()

    override fun getExtensions(): List<String> {
        return listOf("konfig.kts")
    }

    override fun getScriptEngine(): ScriptEngine {
        return KotlinJsr223ScriptEngineImpl(
            this,
            scriptDefinition.compilationConfiguration,
            scriptDefinition.evaluationConfiguration
        ) { ScriptArgsWithTypes(arrayOf(it.getAttribute("konfigScope")), arrayOf(Konfig::class)) }
    }
}
