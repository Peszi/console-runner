package com.test.main.service

import com.test.main.Console
import com.test.main.ScriptEngine
import java.util.*

internal class ScriptService {

    val fileService = FileService()

    init {
//        ScriptEngine.instance.runScriptFile("script.js")
        Console.start(this)
    }

    companion object {

        @JvmStatic
        fun fun1(name: String): String {
            System.out.format("Hi there from Java, %s", name)
            return "greetings from java"
        }

        @JvmStatic
        fun runScript(script: String) {
            ScriptEngine.instance.runScript(script)
        }

        @JvmStatic
        fun runScriptFile(name: String) {
            ScriptEngine.instance.runScriptFile(name)
        }

        @JvmStatic
        fun readLine(): String {
            val scanner = Scanner(System.`in`); scanner.hasNext()
            return scanner.nextLine()
        }
    }
}