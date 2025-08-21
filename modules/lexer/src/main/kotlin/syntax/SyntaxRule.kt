package syntax

interface SyntaxRule {
    fun match(
        line: String,
        index: Int,
        row: Int,
    ): Exception?
}
