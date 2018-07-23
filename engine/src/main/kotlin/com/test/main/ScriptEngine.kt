package com.test.main

import java.io.File
import java.io.InputStreamReader
import java.io.PrintWriter
import javax.script.ScriptEngineManager

const val SCRIPTS_LIMIT = 50

internal class ScriptEngine private constructor(): Runnable {

    private val scriptEngine = ScriptEngineManager().getEngineByName("nashorn")
    private var scriptsStarted = 0

    init {
        scriptEngine.context.writer = PrintWriter(System.out, true)
        scriptEngine.context.reader = InputStreamReader(System.`in`)
        Thread(this).start()
    }

    private object Holder { val INSTANCE = ScriptEngine() }

    fun runScriptFile(fileName: String) {
        runScript( File("engine/$fileName").readText(Charsets.UTF_8))
    }

    fun runScript(scriptContent: String) {
        try {
            println("[Script]")
            if (scriptsStarted >= SCRIPTS_LIMIT) return
            scriptsStarted++; scriptEngine.eval(getBaseScript() + scriptContent); scriptsStarted--
            println("[Console]")
        } catch (e: RuntimeException) {
            System.err.println(e.message)
        }
    }

    override fun run() {
//        while (true) {
//            Thread.sleep(1000)
//            println("SCRIPTS $scriptsStarted")
//            if (scriptsStarted == 0) break
//        }
    }

    companion object {

        val instance: ScriptEngine by lazy { ScriptEngine.Holder.INSTANCE }

        fun getBaseScript() = "var Framework = Java.type('com.test.main.service.ScriptService');"

    }
}