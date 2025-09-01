package syntax.rules

import exception.InvalidCamelCaseException
import syntax.LinterRule

class CamelCaseRule : LinterRule {
    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): Exception? {
        if (line.matches(Regex("\\b[a-z][a-z0-9]*(?:[A-Z0-9]+[a-z0-9]*)*\\b"))) {
            return null
        }
        return InvalidCamelCaseException("Invalid camelCase identifier at row $row, index $index: $line")
    }
}
