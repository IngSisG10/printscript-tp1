package linter

import data.LinterData
import linter.syntax.LinterRule
import token.abs.TokenInterface

class Linter(
    private val linterRules: List<LinterRule> = emptyList(),
) {
    fun lint(tokens: List<TokenInterface>) {
        for (rule in linterRules) {
            val res = rule.match(tokens)
            if (res != null) {
                throw res
            }
        }
    }

    fun formatterLint(tokens: List<TokenInterface>): List<LinterData> {
        val list = mutableListOf<LinterData>()
        for (rule in linterRules) {
            val res = rule.matchWithData(tokens)
            list.addAll(res)
        }
        return list
    }
}
