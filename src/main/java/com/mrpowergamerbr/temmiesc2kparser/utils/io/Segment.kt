package com.mrpowergamerbr.temmiesc2kparser.utils.io

import com.mrpowergamerbr.temmiesc2kparser.utils.SC2KBinaryStream
import java.nio.ByteBuffer

class Segment {
    companion object {
        var MAX_BUFFER_SIZE = 65536;
    }

    var name: String;
    var raw: ByteArray;
    var decompressed: ByteArray? = null;
    var binaryStream: SC2KBinaryStream;

    constructor(name: String, raw: ByteArray) {
        this.name = name;
        this.raw = raw;
        this.binaryStream = SC2KBinaryStream(raw);
    }

    // From https://github.com/rakama/Minecraft-SC2MC/blob/master/src/rakama/sc2mc/io/Segment.java#L47
    fun decompress() {
        val buffer = ByteArray(MAX_BUFFER_SIZE)

        var size = 0
        var index = 0
        while (index < raw.size) {
            var count = 0xFF and raw[index++].toInt()

            if (count == 0 || count == 128)
                throw RuntimeException("Invalid compression format!")

            if (count < 128) {
                for (j in 0..count - 1)
                    buffer[size++] = raw[index++]
            } else {
                count -= 127
                val value = raw[index++]
                for (j in 0..count - 1)
                    buffer[size++] = value
            }
        }

        decompressed = ByteArray(size)
        System.arraycopy(buffer, 0, decompressed, 0, size)
    }
}