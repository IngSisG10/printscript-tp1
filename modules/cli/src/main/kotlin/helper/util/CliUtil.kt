package helper.util

import java.io.File

interface CliUtil {
    fun tryFindFile(filename: String): String? {
        val file = File(filename)

        if (!file.exists()) {
            println("file '$filename' not found")
            return null
        }

        return file.readText()
    }
}
