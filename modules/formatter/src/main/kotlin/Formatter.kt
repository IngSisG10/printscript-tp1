import token.abs.TokenInterface

class Formatter(
    val tokens: List<TokenInterface>,
) {
    private val formatterRules: List<FormatterFix> =
        listOf(
            fixes.SpaceBeforeColon(),
            fixes.SpaceAfterColon(),
        )

    private val linter = Linter()

    fun format(code: String): String {
        val issues: List<LinterData> = linter.formatterLint(tokens)

        for (issue in issues) {
            for (rule in formatterRules) {
                if (rule.canFix(issue.exception)) {
                    return rule.fix(issue.exception, tokens)
                }
            }
        }

        return code // return the original code if no fix is applied
    }
}
