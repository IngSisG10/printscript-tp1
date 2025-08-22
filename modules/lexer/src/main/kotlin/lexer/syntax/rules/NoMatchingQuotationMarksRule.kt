package lexer.syntax.rules

import exception.NoMatchingQuotationMarksException
import lexer.syntax.SyntaxRule

class NoMatchingQuotationMarksRule : SyntaxRule {
    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): Exception? {
        val singleQuotes = line.count { it == '\'' }
        val doubleQuotes = line.count { it == '\"' }
        if (singleQuotes % 2 != 0 || doubleQuotes % 2 != 0) {
            return NoMatchingQuotationMarksException("No matching quotation marks found at row $row, index $index")
        }
        return null
    }
}
