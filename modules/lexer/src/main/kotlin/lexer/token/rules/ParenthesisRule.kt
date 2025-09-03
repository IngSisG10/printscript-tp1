package lexer.token.rules

import exception.NoMatchingParenthesisException
import lexer.token.TokenRule
import token.CloseParenthesisToken
import token.OpenParenthesisToken

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
        if (line[index] == '(') {
            val token = OpenParenthesisToken(row, index)
            return TokenRule.MatchResult(
                token,
                index + 1,
            )
        } else if (line[index] == ')') {
            val token = CloseParenthesisToken(row, index)
            return TokenRule.MatchResult(
                token,
                index + 1,
            )
        }
        return null
    }
}
