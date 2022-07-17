package ru.potatophobe.konfig

import org.jetbrains.kotlin.cli.common.repl.KotlinJsr223JvmScriptEngineFactoryBase
import org.jetbrains.kotlin.cli.common.repl.ScriptArgsWithTypes
import java.io.File
import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.SimpleScriptContext
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.createJvmScriptDefinitionFromTemplate
import kotlin.script.experimental.jvmhost.jsr223.KotlinJsr223ScriptEngineImpl

@KotlinScript(
    fileExtension = "konfig.kts",
    compilationConfiguration = KonfigKtsCompilationConfiguration::class
)
abstract class KonfigKts

object KonfigKtsCompilationConfiguration : ScriptCompilationConfiguration(
    {
        implicitReceivers(KonfigScope::class)
        jvm { dependenciesFromCurrentContext(wholeClasspath = true) }
        ide { acceptedLocations(ScriptAcceptedLocation.Project) }
    }
)

class KonfigKtsEngineFactory : KotlinJsr223JvmScriptEngineFactoryBase() {
    private val scriptDefinition = createJvmScriptDefinitionFromTemplate<KonfigKts>()

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

class KonfigKtsFactory {
    companion object {
        private const val defaultKonfigKtsName = "application.konfig.kts"
    }

    private val scriptEngine = ScriptEngineManager().getEngineByExtension("konfig.kts")

    fun load(): Konfig {
        return load(defaultKonfigKtsName)
    }

    fun load(name: String): Konfig {
        return load(File(Thread.currentThread().contextClassLoader.getResource(name)?.file!!))
    }

    fun load(file: File): Konfig {
        return konfig {
            scriptEngine.eval(file.bufferedReader(), SimpleScriptContext().apply {
                setAttribute("konfigScope", this@konfig, ScriptContext.ENGINE_SCOPE)
            })
        }
    }
}
