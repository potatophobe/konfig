package ru.potatophobe.konfig.kts

import ru.potatophobe.konfig.api.KonfigScope
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

/**
 * Script definition to support Konfig through Kotlin Script
 *
 * @see KonfigKtsScriptEngineFactory
 * */
@KotlinScript(fileExtension = "konfig.kts", compilationConfiguration = KonfigKtsCompilationConfiguration::class)
abstract class KonfigKtsScript

class KonfigKtsCompilationConfiguration : ScriptCompilationConfiguration(
    {
        implicitReceivers(KonfigScope::class)
        jvm { dependenciesFromCurrentContext(wholeClasspath = true) }
        ide { acceptedLocations(ScriptAcceptedLocation.Project) }
    }
)
