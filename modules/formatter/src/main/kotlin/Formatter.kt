import data.LinterData
import token.abs.TokenInterface

class Formatter(
    private val linter: Linter = Linter(),
) {
    private val formatterRules: List<FormatterFix> =
        listOf(
            fixes.SpaceBeforeColon(),
            fixes.SpaceAfterColon(),
        )

    fun format(tokens: List<TokenInterface>): String {
        val issues: List<LinterData> = linter.formatterLint(tokens)
        if (issues.isEmpty()) return convert(tokens)
        var newTokenList: List<TokenInterface> = tokens
        for (issue in issues) {
            var fixed = false
            for (rule in formatterRules) {
                if (rule.canFix(issue)) {
                    fixed = true
                    newTokenList = rule.fix(issue, newTokenList)
                }
            }
            if (!fixed) {
                throw issue.exception
            }
        }

        // return the original code if no fix is applied
        return convert(newTokenList)
    }

    private fun convert(tokens: List<TokenInterface>): String {
        val builder = StringBuilder()
        for (token in tokens) {
            builder.append(token.value)
        }
        return builder.toString()
    }
}
