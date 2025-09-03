package cli.helper.util

import lexer.util.LexerUtil
import linter.util.LinterUtil
import java.io.File

interface CliUtil :
    LexerUtil,
    LinterUtil {
    fun findFile(filename: String): String? {
        val file = File(filename)

        if (!file.exists()) {
            println("file '$filename' not found")
            return null
        }

        return file.readText()
    }
}
