package linter

import common.data.LinterData
import common.token.abs.TokenInterface
import linter.syntax.LinterRule

class Linter(
    private val linterRules: List<LinterRule> = emptyList(),
) {
    fun lint(tokens: List<common.token.abs.TokenInterface>) {
        for (rule in linterRules) {
            val res = rule.match(tokens)
            if (res != null) {
                throw res
            }
        }
    }

    fun formatterLint(tokens: List<common.token.abs.TokenInterface>): List<common.data.LinterData> {
        val list = mutableListOf<common.data.LinterData>()
        for (rule in linterRules) {
            val res = rule.matchWithData(tokens)
            list.addAll(res)
        }
        return list
    }
}
