package linter

import common.token.abs.TokenInterface
import linter.rules.abs.LinterRule

class Linter(
    private val linterRules: List<LinterRule> = emptyList(),
) {
    fun lint(tokens: List<TokenInterface>): List<Throwable> {
        val errorList = mutableListOf<Throwable>()
        for (rule in linterRules) {
            val res = rule.match(tokens)
            if (res != null) {
                errorList.addAll(res)
            }
        }
        return errorList.toList()
    }
}
