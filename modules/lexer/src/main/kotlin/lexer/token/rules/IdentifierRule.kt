package lexer.token.rules

import Linter
import lexer.token.TokenRule
import token.VariableToken

class IdentifierRule(
    private val linter: Linter,
) : TokenRule {
    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): TokenRule.MatchResult? {
        if (index < 0 || index >= line.length) return null

        val ch = line[index]
        // Allow letter or '_' as start
        if (!(ch.isLetter() || ch == '_')) return null

        var i = index + 1
        // Allow letters, digits, or '_'
        while (i < line.length && (line[i].isLetterOrDigit() || line[i] == '_')) {
            i++
        }

        linter.lint(line, index, row)

        val text = line.substring(index, i)

        return TokenRule.MatchResult(VariableToken(text, row, index), i)
    }
}
