package lexer

import exception.UnknownExpressionException
import lexer.syntax.SyntaxRule
import lexer.syntax.rules.NoMatchingParenthesisRule
import lexer.token.TokenRule
import lexer.token.rules.IdentifierRule
import lexer.token.rules.KeywordRule
import lexer.token.rules.NumberLiteralRule
import lexer.token.rules.ParenthesisRule
import lexer.token.rules.SingleCharRule
import lexer.token.rules.StringLiteralRule
import token.abs.TokenInterface

class Lexer(
    private val code: String,
    private val tokenRules: List<TokenRule> =
        listOf(
            StringLiteralRule(),
            NumberLiteralRule(),
            KeywordRule(),
            ParenthesisRule(),
            SingleCharRule(),
            IdentifierRule(),
        ),
    private val syntaxRules: List<SyntaxRule> =
        listOf(
            NoMatchingParenthesisRule(),
        ),
) {
    private val tokens = mutableListOf<TokenInterface>()
    private var row = 0

    fun lex(): List<TokenInterface> {
        tokenize(code)
        return tokens
    }

    private fun tokenize(text: String) {
        var i = 0
        while (i < text.length) {
            val c = text[i]
            when {
                c == '\n' -> {
                    row++
                    i++
                }
                c.isWhitespace() -> i++
                else -> {
                    var matched = false
                    for (syntax in syntaxRules) {
                        val res = syntax.match(text, i, row)
                        if (res != null) {
                            throw res
                        }
                    }
                    for (rule in tokenRules) {
                        val res = rule.match(text, i, row)
                        if (res != null) {
                            res.token?.let { tokens.add(it) }
                            i = res.nextIndex
                            row += res.rowDelta
                            matched = true
                            break
                        }
                    }
                    if (!matched) {
                        throw UnknownExpressionException(
                            "Unknown expression at row $row, column $i: '${text.substring(i)}'",
                        )
                    }
                }
            }
        }
    }
}
