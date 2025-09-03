package lexer.util

import lexer.Lexer
import lexer.token.TokenRule
import lexer.token.rules.IdentifierRule
import lexer.token.rules.KeywordOnePointOneRule
import lexer.token.rules.KeywordRule
import lexer.token.rules.NumberLiteralRule
import lexer.token.rules.ParenthesisRule
import lexer.token.rules.SingleCharRule
import lexer.token.rules.StringLiteralRule

interface LexerUtil {
    fun createLexer(version: String): Lexer {
        val versionOnePointOne =
            listOf<TokenRule>(
                StringLiteralRule(),
                NumberLiteralRule(),
                KeywordRule(),
                KeywordOnePointOneRule(),
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
