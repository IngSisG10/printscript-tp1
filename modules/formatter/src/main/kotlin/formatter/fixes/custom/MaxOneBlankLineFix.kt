package formatter.fixes.custom

import common.token.NewLineToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix

class MaxOneBlankLineFix : FormatterFix {
    override fun getName(): String = "max_one_blank_line_fix"

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val result = mutableListOf<TokenInterface>()
        var consecutiveNewLines = 0

        for (token in tokens) {
            if (token is NewLineToken) {
                consecutiveNewLines++
                if (consecutiveNewLines <= 2) {
                    result.add(token) // \n\n
                }
            } else {
                consecutiveNewLines = 0
                result.add(token) // reset counter on non-newline token & add other tokens
            }
        }

        return result
    }
}
