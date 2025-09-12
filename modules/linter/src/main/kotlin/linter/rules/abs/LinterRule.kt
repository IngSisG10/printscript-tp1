package linter.syntax

import common.token.abs.TokenInterface

interface LinterRule {
    fun getName(): String

    fun match(tokens: List<TokenInterface>): Exception?
}
