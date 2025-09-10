package lexer

import common.exception.UnknownExpressionException
import common.token.NewLineToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import common.util.segmentsBySemicolon
import lexer.token.TokenRule
import lexer.token.rules.IdentifierRule
import lexer.token.rules.KeywordRule
import lexer.token.rules.NumberLiteralRule
import lexer.token.rules.ParenthesisRule
import lexer.token.rules.SingleCharRule
import lexer.token.rules.StringLiteralRule
import java.io.InputStream

class Lexer(
    private val tokenRules: List<TokenRule> =
        listOf(
            StringLiteralRule(),
            NumberLiteralRule(),
            KeywordRule(),
            ParenthesisRule(),
            SingleCharRule(),
            IdentifierRule(),
        ),
) {
    private val tokens = mutableListOf<TokenInterface>()
    private var row = 0

    fun lex(input: InputStream): List<TokenInterface> {
        input.segmentsBySemicolon().forEach { segment ->
            tokenize(segment)
        }
        return tokens
    }

    private fun tokenize(text: String) {
        var i = 0
        while (i < text.length) {
            val c = text[i]
            when {
                c == '\n' -> {
                    tokens.add(NewLineToken(row, i))
                    row++
                    i++
                }
                c.isWhitespace() -> {
                    tokens.add(WhiteSpaceToken(row, i))
                    i++
                }
                else -> {
                    var matched = false
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
