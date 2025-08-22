package lexer.token.rules

import lexer.syntax.SyntaxRule
import lexer.token.TokenRule
import token.VariableToken


class IdentifierRule(
    private val rules : List<SyntaxRule> = emptyList()
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

        val text = line.substring(index, i)

        for (rule in rules) {
            val exception = rule.match(text, index, row)
            if (exception != null) {
                throw exception
            }
        }

        return TokenRule.MatchResult(VariableToken(text, row, index), i)
    }
}
