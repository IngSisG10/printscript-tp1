package formatter.rules.abs

import common.data.FormatterData
import common.token.abs.TokenInterface

interface FormatterRule {
    fun getName(): String
    fun matchWithData(tokens: List<TokenInterface>): List<FormatterData>
}