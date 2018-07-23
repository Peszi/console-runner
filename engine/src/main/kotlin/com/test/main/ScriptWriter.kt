package com.test.main

import java.io.Writer

internal class ScriptWriter: Writer() {

    private var bufferLine = ""

    override fun write(cbuf: CharArray?, off: Int, len: Int) {
        cbuf?.let { bufferLine += String(it, off, len) }
    }

    override fun flush() {
        print("> $bufferLine"); bufferLine = ""
    }

    override fun close() {}
}
