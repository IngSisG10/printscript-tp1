import syntax.LinterRule

class Linter(
    val linterRules: List<LinterRule> = emptyList(),
) {
    fun lint(
        text: String,
        i: Int,
        row: Int,
    ) {
        for (rule in linterRules) {
            val res = rule.match(text, i, row)
            if (res != null) {
                throw res
            }
        }
    }
}
