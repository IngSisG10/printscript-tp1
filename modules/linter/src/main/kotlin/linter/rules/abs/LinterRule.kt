package linter.rules.abs

import common.token.abs.TokenInterface
import kotlinx.serialization.json.JsonElement

interface LinterRule {
    fun applies(rules: Map<String, JsonElement>): Boolean

    fun match(tokens: List<TokenInterface>): List<Throwable>?
}
