package com.mrpowergamerbr.temmiesc2kparser.utils

import java.nio.ByteBuffer
import kotlin.experimental.and

class SC2KBinaryStream {
    val buffer: ByteBuffer;

    constructor(byteArray: ByteArray) {
        this.buffer = ByteBuffer.wrap(byteArray)
    }

    // Read string from ByteBuffer
    fun readString(length: Int): String {
        var str = "";
        for (i in buffer.position()..buffer.position() + length - 1) {
            str += buffer.get().toChar();
        }
        return str;
    }

    fun skip(skip: Int) {
        buffer.position(buffer.position() + skip)
    }
}