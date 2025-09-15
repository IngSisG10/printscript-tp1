package formatter.fixes.required

import common.enums.OperationEnum
import common.token.OperationToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement

class NoSpaceAfterEqualFix : FormatterFix {
    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean =
        fixesIWantToApply.containsKey("enforce-no-spacing-around-equals") &&
            fixesIWantToApply["enforce-no-spacing-around-equals"]?.toString()?.toBoolean() == true

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()
        for ((i, token) in tokens.withIndex()) {
            if (token is OperationToken && token.value == OperationEnum.EQUAL) {
                while (true) {
                    val nextIndex = i + 1
                    val previousIndex = i - 1
                    if (nextIndex < mutableTokens.size && mutableTokens[nextIndex] is WhiteSpaceToken) {
                        mutableTokens.removeAt(nextIndex)
                    } else if (previousIndex >= 0 && mutableTokens[previousIndex] is WhiteSpaceToken) {
                        mutableTokens.removeAt(previousIndex)
                    } else {
                        break
                    }
                }
            }
        }
        return mutableTokens
    }
}
