package linter

import common.data.FormatterData
import common.token.abs.TokenInterface
import linter.rules.abs.LinterRule

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

    fun formatterLint(tokens: List<TokenInterface>): List<FormatterData> {
        val list = mutableListOf<FormatterData>()
        for (rule in linterRules) {
            val res = rule.matchWithData(tokens)
            list.addAll(res)
        }
        return list
    }
}
