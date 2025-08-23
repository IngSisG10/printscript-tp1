package lexer.syntax.rules

import exception.InvalidCamelCaseException
import lexer.syntax.SyntaxRule

class CamelCaseRule : SyntaxRule {
    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): Exception? {
        if (line.matches(Regex("\\b[a-z]+(?:[A-Z][a-z0-9]*)+\\b"))) {
            return null
        }
        return InvalidCamelCaseException("Invalid camelCase identifier at row $row, index $index: $line")
    }
}
