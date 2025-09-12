package formatter.fixes

import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix

class IfBracePlacementFix : FormatterFix {
    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()
        TODO()
    }
}