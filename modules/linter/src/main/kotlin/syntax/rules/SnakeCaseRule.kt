package syntax.rules

import exception.InvalidSnakeCaseException
import syntax.LinterRule

class SnakeCaseRule : LinterRule {
    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): Exception? {
        if (line.matches(Regex("\\b[a-z0-9]+(?:_[a-z0-9]+)*\\b"))) {
            return null
        }
        return InvalidSnakeCaseException("Invalid snake_case identifier at row $row, index $index: $line")
    }
}
