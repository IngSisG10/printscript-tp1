package linter.rules.abs

import common.token.abs.TokenInterface
import kotlinx.serialization.json.JsonElement
import linter.dto.LinterDto

interface LinterRule {
    fun applies(rules: Map<String, JsonElement>): Boolean

    fun match(tokens: List<TokenInterface>): List<Throwable>?

    fun getRuleNameAndValue(): LinterDto
}
