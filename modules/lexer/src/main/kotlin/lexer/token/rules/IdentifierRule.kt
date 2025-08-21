package lexer.token.rules

import lexer.token.TokenRule
import token.VariableToken

class IdentifierRule : TokenRule {
    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): TokenRule.MatchResult? {
        if (!line[index].isLetter()) return null
        var i = index
        while (i < line.length && line[i].isLetterOrDigit()) i++
        val text = line.substring(index, i)
        return TokenRule.MatchResult(VariableToken(text, row, index), i)
    }
}
