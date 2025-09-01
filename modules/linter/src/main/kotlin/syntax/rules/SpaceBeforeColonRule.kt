package syntax.rules

import exception.NoSpaceBeforeColonException
import syntax.LinterRule

class SpaceBeforeColonRule : LinterRule {
    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): Exception? {
        val char = line[index]
        if (char == ':') {
            val charBefore = line[index - 1]
            if (charBefore != ' ') throw NoSpaceBeforeColonException()
            return null
        }
        return null
    }
}
