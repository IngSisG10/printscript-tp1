package lexer.syntax.rules

import exception.InvalidPascalCaseException
import lexer.syntax.SyntaxRule

class PascalCaseRule : SyntaxRule {
    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): Exception? {
        if (line.matches(Regex("\\b(?:[A-Z][a-z0-9]*)+\\b"))) {
            return null
        }
        return InvalidPascalCaseException("Invalid PascalCase identifier at row $row, index $index: $line")
    }
}
