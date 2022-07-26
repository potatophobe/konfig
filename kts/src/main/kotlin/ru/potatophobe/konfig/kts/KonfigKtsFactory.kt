package ru.potatophobe.konfig.kts

import ru.potatophobe.konfig.api.Konfig
import ru.potatophobe.konfig.impl.konfig
import java.io.File
import javax.script.ScriptContext
import javax.script.ScriptEngineManager
import javax.script.SimpleScriptContext

/**
 * Factory creates Konfig from `*.konfig.kts` file
 *
 * @see Konfig
 * */
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
