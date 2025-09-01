import data.LinterData
import token.abs.TokenInterface

class Formatter(
    private val tokens: List<TokenInterface>,
) {
    private val formatterRules: List<FormatterFix> =
        listOf(
            fixes.SpaceBeforeColon(),
            fixes.SpaceAfterColon(),
        )

    private val linter = Linter()

    fun format(): String {
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
        return convert(tokens)
    }

    private fun convert(tokens: List<TokenInterface>): String {
        val builder = StringBuilder()
        for (token in tokens) {
            builder.append(token.value)
        }
        return builder.toString()
    }
}
