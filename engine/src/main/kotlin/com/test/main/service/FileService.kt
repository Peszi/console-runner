package com.test.main.service

import java.io.File
import java.nio.file.Paths

internal class FileService {

    private val baseDir = Paths.get("").toAbsolutePath().toFile().absolutePath
    var workingDir = baseDir

    fun changeDir(dir: String) {
        if (dir.isNotEmpty()) {
            File(workingDir).listFiles()
                    .filter { it.isDirectory }
                    .firstOrNull { it.name.equals(dir, true) }
                    ?.let { workingDir = it.absolutePath; println("DIR $dir") } ?: println("Incorrect dir name!")
        } else {
            if (workingDir == baseDir) return
            workingDir = workingDir.substringBeforeLast("\\")
            println("DIR UP $dir")
        }
    }

    companion object {

        fun getFilesList(dir: String) {
            File(dir).listFiles()
                    .sortedBy { it.isDirectory }
                    .forEach {
                        val message = StringBuilder(createString(64))
                        if (it.isFile) message.insert(0, it.extension.toUpperCase())
                        val name = if (it.isDirectory) it.name.toUpperCase() else it.name.substringBeforeLast(".")
                        message.insert(6, name)
                        println(message)
                    }
        }

        private fun createString(length: Int): String {
            var value = ""; for (i in 1..length) value += " "
            return value
        }
    }
}