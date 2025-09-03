package helper.util

import lexer.Lexer
import lexer.token.TokenRule
import lexer.token.rules.IdentifierRule
import lexer.token.rules.KeywordRule
import lexer.token.rules.NumberLiteralRule
import lexer.token.rules.ParenthesisRule
import lexer.token.rules.SingleCharRule
import lexer.token.rules.StringLiteralRule
import java.io.File

interface CliUtil {
    fun findFile(filename: String): String? {
        val file = File(filename)

        if (!file.exists()) {
            println("file '$filename' not found")
            return null
        }

        return file.readText()
    }

    fun createLexer(version: String): Lexer {
        val versionOnePointOne =
            listOf<TokenRule>(
                StringLiteralRule(),
                NumberLiteralRule(),
                KeywordRule(),
                ParenthesisRule(),
                SingleCharRule(),
                IdentifierRule(),
                // TODO: add new features here
            )

        return when (version) {
            "1.1" -> Lexer(versionOnePointOne)
            "1.0" -> Lexer() // 1.0 is default
            else -> throw Exception("Unsupported version")
        }
    }
}
