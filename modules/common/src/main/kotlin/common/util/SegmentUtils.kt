package common.util

import java.io.InputStream

fun InputStream.segmentsBySemicolon(): Sequence<String> =
    sequence {
        val reader = this@segmentsBySemicolon.bufferedReader()
        val buffer = StringBuilder()

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            var start = 0
            for (i in line!!.indices) {
                if (line!![i] == ';') {
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
