package linter.rules.custom

import common.exception.NoNewLineAfterSemiColon
import common.token.EndSentenceToken
import common.token.NewLineToken
import common.token.abs.TokenInterface
import kotlinx.serialization.json.JsonElement
import linter.rules.abs.LinterRule
import linter.rules.abs.RuleSettings

class LineJumpAfterSemicolonRule :
    LinterRule,
    RuleSettings {
    var lines = 1

    override fun applies(rules: Map<String, JsonElement>): Boolean {
        TODO("Not yet implemented")
    }

    override fun match(tokens: List<TokenInterface>): List<Throwable> {
        val list = mutableListOf<Throwable>()
        for ((index, token) in tokens.withIndex()) {
            if (token is EndSentenceToken) {
                val next = tokens.getOrNull(index + 1)

                if (next !is NewLineToken) {
                    list.add(NoNewLineAfterSemiColon(token.getPosition()))
                }
            }
        }
        return list.toList()
    }

    override fun setRule(options: Map<String, JsonElement>) {
        lines = options["lines"]?.jsonPrimitive?.int ?: 1
    }
}
