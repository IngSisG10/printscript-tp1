package linter.syntax

import common.data.LinterData
import common.token.abs.TokenInterface

interface LinterRule {
    fun getName(): String

    fun match(tokens: List<TokenInterface>): Exception?

    fun matchWithData(tokens: List<TokenInterface>): List<LinterData>
}
