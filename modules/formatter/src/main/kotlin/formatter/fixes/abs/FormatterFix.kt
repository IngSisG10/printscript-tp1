package formatter.fixes.abs

import common.token.abs.TokenInterface

interface FormatterFix {
    fun fix(tokens: List<TokenInterface>): List<TokenInterface>
}
