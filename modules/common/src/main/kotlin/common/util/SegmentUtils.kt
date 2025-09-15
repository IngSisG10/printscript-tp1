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
                if (line!!.isEmpty()) {
                    // This was an empty line in original - add extra newline
                    buffer.append('\n')
                    continue
                }

                var start = 0
                var hasYielded = false
                for (i in line!!.indices) {
                    if (line!![i] == '{') depth++
                    if (line!![i] == '}') depth--
                    if ((line!![i] == ';' || line!![i] == '}') && depth == 0) {
                        buffer.append(line!!.substring(start, i + 1))
                        buffer.append('\n')
                        yield(buffer.toString())
                        buffer.clear()
                        start = i + 1
                        hasYielded = true
                    }
                }
                if (start < line!!.length) {
                    buffer.append(line!!.substring(start))
                }
                // Add newline only if we didn't yield a complete segment
                if (!hasYielded) {
                    buffer.append('\n')
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
