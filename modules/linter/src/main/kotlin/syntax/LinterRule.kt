package syntax

interface LinterRule {
    fun match(
        line: String,
        index: Int,
        row: Int,
    ): Exception?
}
