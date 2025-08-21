package lexer.syntax.rules

import exception.NoMatchingParenthesisException
import lexer.syntax.SyntaxRule

class NoMatchingParenthesisRule : SyntaxRule {
    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): Exception? =
        if (line[index] == '(' || line[index] == ')') {
            NoMatchingParenthesisException("No matching parenthesis found at row $row, index $index")
        } else {
            null
        }
}
