package linter.syntax

import common.data.LinterData
import common.token.abs.TokenInterface

interface LinterRule {
    fun getName(): String

    fun match(tokens: List<common.token.abs.TokenInterface>): Exception?

    fun matchWithData(tokens: List<common.token.abs.TokenInterface>): List<common.data.LinterData>
}
