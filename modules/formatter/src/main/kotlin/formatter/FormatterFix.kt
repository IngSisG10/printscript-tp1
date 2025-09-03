package formatter

import common.data.LinterData
import common.token.abs.TokenInterface

interface FormatterFix {
    fun canFix(issue: common.data.LinterData): Boolean

    fun fix(
        issue: common.data.LinterData,
        tokens: List<common.token.abs.TokenInterface>,
    ): List<common.token.abs.TokenInterface>
}
