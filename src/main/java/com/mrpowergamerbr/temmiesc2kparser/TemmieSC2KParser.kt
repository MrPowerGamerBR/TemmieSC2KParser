package com.mrpowergamerbr.temmiesc2kparser

import com.google.common.io.Files
import com.mrpowergamerbr.temmiesc2kparser.utils.MiscIndex
import com.mrpowergamerbr.temmiesc2kparser.utils.SC2KBinaryStream
import com.mrpowergamerbr.temmiesc2kparser.utils.exceptions.SC2KParserException
import com.mrpowergamerbr.temmiesc2kparser.utils.io.Segment
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.ColorConvertOp
import java.io.File
import java.util.*
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    println("Hello World!");

    var sc2kCity = TemmieSC2KParser.readCity(File("D:\\Games\\DOS\\SC2K\\Cities\\RETREAT.SC2"))

    println("City Name: " + sc2kCity!!.cityName)
    println("City Money Supply: " + sc2kCity.miscellaneous.get(MiscIndex.MONEY_SUPPLY))
    println("Mayor: " + sc2kCity.labels.get(0))

    var image = BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB)

    var x = 0;
    var y = 0;

    for (x in 0..127) {
        for (y in 0..127) {
            var bld = sc2kCity.buildings[x, y];

            var asInt = Integer.parseInt(bld, 16);
            if (asInt >= 29 && 43 >= asInt) {
                image.setRGB(x, y, Color.GREEN.rgb)
            }
        }
    }

    ImageIO.write(image, "png", File("D:\\sc2k_test.png"))
}

class TemmieSC2KParser {
    companion object {
        @JvmStatic
        fun readCity(file: File): SC2KCity? {
            var city = SC2KCity()

            val bytes = Files.toByteArray(file); // And read it to a byte array!

            val binaryStream = SC2KBinaryStream(bytes);
            // http://djm.cc/simcity-2000-info.txt

            // Read IFF header
            var iffHeader = binaryStream.readString(4);
            // Read 4 bytes
            binaryStream.skip(4); // We don't need to read those 4 bytes
            // Read file header
            var fileHeader = binaryStream.readString(4);

            if (iffHeader != "FORM" || fileHeader != "SCDH") {
                throw SC2KParserException("Invalid or corrupt SimCity 2000 save file!")
            }

            while (binaryStream.buffer.hasRemaining()) {
                var segmentName = binaryStream.readString(4)
                var length = binaryStream.buffer.getInt()

                var segmentData = Arrays.copyOfRange(binaryStream.buffer.array(), binaryStream.buffer.position(), binaryStream.buffer.position() + length);
                var segment = Segment(segmentName, segmentData)

                if (segmentName == "CNAM") { // City Name
                    var cityNameLength = segment.binaryStream.buffer.get().toInt()

                    var name = segment.binaryStream.readString(cityNameLength);

                    // Remove garbage characters that are at the end of the name. - https://github.com/dwfennell/city-parser-2000/blob/master/CityParser.cs#L543
                    var cut = 0;
                    for (byte in name.toByteArray()) {
                        cut++;
                        if (byte.toHex() == "0") {
                            break;
                        }
                    }

                    name = name.substring(0, cut);
                    city.cityName = name;
                } else if (segmentName == "XBLD") { // Building data
                    segment.decompress(); // Decompress...

                    var decompressed = segment.decompressed;
                    if (decompressed == null) { return null; }
                    var binaryStream = SC2KBinaryStream(decompressed)

                    var x = 0;
                    var y = 0;
                    while (binaryStream.buffer.hasRemaining()) {
                        if (y > 127) {
                            x += 1;
                            y = 0;
                        }
                        val value = binaryStream.buffer.get()
                        city.buildings[x, y] = value.toHex();
                        y++;
                    }
                } else if (segmentName == "XLAB") { // 256 Labels
                    segment.decompress(); // Decompress...

                    var decompressed = segment.decompressed;
                    if (decompressed == null) { return null; }
                    var binaryStream = SC2KBinaryStream(decompressed)

                    var labelIdx = 0;
                    while (binaryStream.buffer.hasRemaining()) {
                        // Labels consist of a 1-byte count followed by the appropriate number of bytes of string
                        var stringLength = binaryStream.buffer.get();
                        var label = binaryStream.readString(24); // While the length is above ^, all strings are padded
                        label = label.substring(0, stringLength.toInt()) // So now we remove the padding

                        city.labels.put(labelIdx, label)
                        labelIdx++;
                    }
                } else if (segmentName == "MISC") { // Misc
                    segment.decompress(); // Decompress...

                    var decompressed = segment.decompressed;
                    if (decompressed == null) { return null; }
                    var binaryStream = SC2KBinaryStream(decompressed)

                    var idx = 0;
                    while (binaryStream.buffer.hasRemaining()) {
                        val value = binaryStream.buffer.getInt()

                        city.miscellaneous.put(idx, value)
                        idx++;
                    }
                } else {
                    // Unknown Segment
                    println("Unknown Segment ${segmentName}, skipping...")
                }
                binaryStream.skip(length)
            }
            return city;
        }
    }
}

fun Byte.toHex(): String {
    return String.format("%X", this);
}

fun List<Byte>.toCharString(): String {
    var str = "";

    for (byte in this) {
        str = str.plus(byte.toChar())
    }

    return str;
}