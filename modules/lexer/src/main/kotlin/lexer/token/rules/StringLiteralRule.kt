package lexer.token.rules

import exception.NoMatchingQuotationMarksException
import lexer.token.TokenRule
import token.StringLiteralToken

class StringLiteralRule : TokenRule {
    private val regex = Regex("\"(.*?)\"")

    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): TokenRule.MatchResult? {
        val singleQuotes = line.count { it == '\'' }
        val doubleQuotes = line.count { it == '\"' }
        if (singleQuotes % 2 != 0 || doubleQuotes % 2 != 0) {
            throw NoMatchingQuotationMarksException("No matching quotation marks found at row $row, index $index")
        }
        val m = regex.find(line, index)?.takeIf { it.range.first == index } ?: return null
        val token = StringLiteralToken(m.groupValues[1], row, index)
        return TokenRule.MatchResult(token, m.range.last + 1)
    }
}
