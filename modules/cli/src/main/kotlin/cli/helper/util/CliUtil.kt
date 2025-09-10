package cli.helper.util

import java.io.File

class CliUtil {
    companion object {
        fun findFile(filename: String): String? {
            val file = File(filename)

            if (!file.exists()) {
                println("file '$filename' not found")
                return null
            }

            return file.readText()
        }
    }
}
