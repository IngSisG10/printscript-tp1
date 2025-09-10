package cli.helper.util

import linter.util.LinterUtil
import java.io.File

interface CliUtil : LinterUtil {
    fun findFile(filename: String): String? {
        val file = File(filename)

        if (!file.exists()) {
            println("file '$filename' not found")
            return null
        }

        return file.readText()
    }
}
