package lexer

import lexer.rules.IdentifierRule
import lexer.rules.KeywordRule
import lexer.rules.NumberLiteralRule
import lexer.rules.ParenthesisRule
import lexer.rules.SingleCharRule
import lexer.rules.StringLiteralRule
import token.abs.TokenInterface

class Lexer(
    private val code: String,
) {
    private val tokens = mutableListOf<TokenInterface>()
    private var row = 0

    private val rules =
        listOf(
            StringLiteralRule(),
            NumberLiteralRule(),
            KeywordRule(),
            ParenthesisRule(),
            SingleCharRule(),
            IdentifierRule(),
        )

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
                    for (rule in rules) {
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
                        // Skip unknown char (could alternatively throw)
                        i++
                    }
                }
            }
        }
    }
}
