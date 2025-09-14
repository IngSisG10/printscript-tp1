package formatter.fixes.required

import common.enums.FunctionEnum
import common.token.FunctionToken
import common.token.NewLineToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FixSettings
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement

class LineJumpSpaceBeforePrintlnFix :
    FormatterFix,
    FixSettings {
    private var maxNewLines = 1

    override fun setFix(fixes: Map<String, JsonElement>) {
        maxNewLines = fixes["line-breaks-after-println"]?.toString()?.toIntOrNull() ?: 1
    }

    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean {
        val configValue = fixesIWantToApply["line-breaks-after-println"]?.toString()?.toIntOrNull()
        return configValue != null && configValue >= 0
    }

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in mutableTokens.indices) {
            val current = mutableTokens[i]

            if (current is FunctionToken && current.value == FunctionEnum.PRINTLN) {
                // Count consecutive newlines before println
                var newLineCount = 0
                var j = i - 1
                while (j >= 0 && mutableTokens[j] is NewLineToken) {
                    newLineCount++
                    j--
                }

                // Too many newlines? Trim to maxNewLines
                if (newLineCount > maxNewLines) {
                    val toRemove = newLineCount - maxNewLines
                    repeat(toRemove) {
                        mutableTokens.removeAt(i - newLineCount)
                    }
                }

                // stop after fixing first println (or remove `return` if you want to fix all)
                return mutableTokens
            }
        }

        return mutableTokens
    }
}
