package common.util

import java.io.InputStream

fun InputStream.segmentsBySemicolon(): Sequence<String> {
    fun rawSegments(): Sequence<String> =
        sequence {
            val reader = this@segmentsBySemicolon.bufferedReader()
            val buffer = StringBuilder()
            var depth = 0
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                var start = 0
                for (i in line!!.indices) {
                    if (line!![i] == '{') depth++
                    if (line!![i] == '}') depth--
                    if ((line!![i] == ';' || line!![i] == '}') && depth == 0) {
                        buffer.append(line!!.substring(start, i + 1))
                        yield(buffer.toString())
                        buffer.clear()
                        start = i + 1
                    }
                }
                if (start < line!!.length) {
                    buffer.append(line!!.substring(start))
                }
            }

            if (buffer.isNotEmpty()) {
                yield(buffer.toString())
            }
        }

    return sequence {
        val iterator = rawSegments().iterator()
        if (!iterator.hasNext()) return@sequence

        var current = iterator.next()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (current.trim().endsWith("}") && next.trim().startsWith("else")) {
                current += next
            } else {
                yield(current)
                current = next
            }
        }
        yield(current)
    }
}

class InputStreamUtil {
    companion object {
        fun segmentsBySemicolon(filename: InputStream): Sequence<String> = filename.segmentsBySemicolon()
    }
}
