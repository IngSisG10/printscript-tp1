import token.abs.TokenInterface

interface FormatterFix {
    fun canFix(issue: LinterData): Boolean

    fun fix(
        issue: LinterData,
        tokens: List<TokenInterface>,
    ): List<TokenInterface>
}
