package formatter.fixes.required

import common.enums.OperationEnum
import common.token.OperationToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement

class SpaceBeforeEqualFix : FormatterFix {
    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean =
        fixesIWantToApply.containsKey("enforce-spacing-around-equals") &&
            fixesIWantToApply["enforce-spacing-around-equals"]?.toString()?.toBoolean() == true

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in 1 until mutableTokens.size) {
            val current = mutableTokens[i]

            if (current is OperationToken && current.value == OperationEnum.EQUAL) {
                val prev = mutableTokens[i - 1]

                if (prev !is common.token.WhiteSpaceToken) {
                    mutableTokens.add(i, common.token.WhiteSpaceToken(current.row, current.position - 1))
                }
            }
        }

        return mutableTokens
    }
}
