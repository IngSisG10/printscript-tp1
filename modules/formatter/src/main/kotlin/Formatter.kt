import data.LinterData
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

    fun format(): String {
        val issues: List<LinterData> = linter.formatterLint(tokens)

        // todo: optimizar para que podamos seguir aplicando reglas hasta que no haya más issues
        for (issue in issues) {
            for (rule in formatterRules) {
                if (rule.canFix(issue)) {
                    val newTokens = rule.fix(issue, tokens)
                    return convert(newTokens)
                }
            }
        }

        // return the original code if no fix is applied
        return convert(tokens)
    }

    // todo: convertir a String
    private fun convert(tokens: List<TokenInterface>): String {
        val builder = StringBuilder()
        for (token in tokens) {
            builder.append(token.value)
        }
        return builder.toString()
    }
}
