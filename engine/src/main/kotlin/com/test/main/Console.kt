package com.test.main

import com.test.main.service.FileService
import com.test.main.service.ScriptService
import java.nio.file.Paths
import java.util.*

internal object Console {

    fun start(scriptService: ScriptService) {
        val scanner = Scanner(System.`in`)
        loop@ while(true) {
            scanner.hasNext()
            val line = scanner.nextLine()
            when {
                // Main
                line.startsWith("exit") -> break@loop
                line.startsWith("run") -> ScriptEngine.instance.runScriptFile("script.js")
                // Files
                line.startsWith("dir") -> scriptService.fileService.changeDir(getArg(line, 1))
                line.startsWith("ls") -> FileService.getFilesList(scriptService.fileService.workingDir)
                else -> println("Unknown command!")
            }
        }
    }

    private fun getArg(line: String, idx: Int): String {
        val args = line.split(" ")
        if (args.size > idx) return args[idx]
        return ""
    }

}