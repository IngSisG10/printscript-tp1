package formatter.fixes.abs

import common.token.abs.TokenInterface

interface FormatterFix {
    fun getName(): String

    fun fix(tokens: List<TokenInterface>): List<TokenInterface>
}
