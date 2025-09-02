package lexer.token.rules

import exception.NoMatchingParenthesisException
import lexer.Lexer
import lexer.token.TokenRule
import token.ParenthesisData
import token.ParenthesisToken
import token.abs.TokenInterface

class ParenthesisRule : TokenRule {
    private fun checkIfCorrectParenthesis(
        line: String,
        index: Int,
        row: Int,
    ): Exception? {
        if (index < 0 || index >= line.length) return null

        return when (line[index]) {
            '(' ->
                if (!hasMatchingClosing(line, index)) {
                    NoMatchingParenthesisException("No matching parenthesis found at row $row, index $index")
                } else {
                    null
                }

            ')' ->
                if (!hasMatchingOpening(line, index)) {
                    NoMatchingParenthesisException("No matching parenthesis found at row $row, index $index")
                } else {
                    null
                }

            else -> null
        }
    }

    private fun hasMatchingClosing(
        line: String,
        start: Int,
    ): Boolean {
        var depth = 0
        for (i in start until line.length) {
            when (line[i]) {
                '(' -> depth++
                ')' -> {
                    depth--
                    if (depth == 0) return true
                }
            }
        }
        return false
    }

    private fun hasMatchingOpening(
        line: String,
        start: Int,
    ): Boolean {
        var depth = 0
        for (i in start downTo 0) {
            when (line[i]) {
                ')' -> depth++
                '(' -> {
                    depth--
                    if (depth == 0) return true
                }
            }
        }
        return false
    }

    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): TokenRule.MatchResult? {
        checkIfCorrectParenthesis(line, index, row)
        if (line[index] != '(') return null
        val data = extractFirstParenthesisWithNesting(line, index, row) ?: return null
        val innerTokens: List<TokenInterface> = Lexer(data.parenthesisData).lex()
        val tok =
            ParenthesisToken(
                value = innerTokens,
                row = row,
                position = index,
                closePosition = data.endParenthesis,
            )
        return TokenRule.MatchResult(tok, data.endParenthesis + 1, rowDelta = data.rowDelta)
    }

    private fun extractFirstParenthesisWithNesting(
        s: String,
        idx: Int,
        startRow: Int,
    ): ParenthesisData? {
        var depth = 0
        var start = -1
        var row = startRow
        for (i in idx until s.length) {
            val ch = s[i]
            when (ch) {
                '(' -> {
                    if (depth == 0) start = i
                    depth++
                }
                '\n' -> row++
                ')' -> {
                    depth--
                    if (depth == 0 && start != -1) {
                        return ParenthesisData(
                            parenthesisData = s.substring(start + 1, i),
                            endParenthesis = i,
                            rowDelta = row - startRow,
                        )
                    }
                }
            }
        }
        return null
    }
}
