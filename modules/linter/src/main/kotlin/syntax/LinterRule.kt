package syntax

import data.LinterData
import token.abs.TokenInterface

interface LinterRule {
    fun getName(): String

    fun match(tokens: List<TokenInterface>): Exception?

    fun matchWithData(tokens: List<TokenInterface>): List<LinterData>
}
