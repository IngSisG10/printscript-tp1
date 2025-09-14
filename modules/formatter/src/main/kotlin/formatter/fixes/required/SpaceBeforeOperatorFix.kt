package formatter.fixes.required

import common.token.OperationToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement

class SpaceBeforeOperatorFix : FormatterFix {
    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean =
        fixesIWantToApply.containsKey("mandatory-space-surrounding-operations") &&
            fixesIWantToApply["mandatory-space-surrounding-operations"]?.toString()?.toBoolean() == true

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in 1 until mutableTokens.size) {
            val current = mutableTokens[i]

            if (current is OperationToken) {
                val prev = mutableTokens[i - 1]

                if (prev !is WhiteSpaceToken) {
                    mutableTokens.add(i, WhiteSpaceToken(current.row, current.position - 1))
                }
            }
        }

        return mutableTokens
    }
}
