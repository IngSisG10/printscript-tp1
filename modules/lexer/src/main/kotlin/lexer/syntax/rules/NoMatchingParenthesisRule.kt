// Kotlin
package lexer.syntax.rules

import exception.NoMatchingParenthesisException
import lexer.syntax.SyntaxRule

class NoMatchingParenthesisRule : SyntaxRule {
    override fun match(
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
}
