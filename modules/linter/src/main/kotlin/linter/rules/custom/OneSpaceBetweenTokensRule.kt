package linter.rules.custom

import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import exception.MoreThanOneSpaceAfterTokenException
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive
import linter.dto.LinterDto
import linter.rules.abs.LinterRule

class OneSpaceBetweenTokensRule : LinterRule {
    override fun applies(rules: Map<String, JsonElement>): Boolean {
        for ((key, value) in rules) {
            if (key == "one-space-between-tokens") {
                if (value.jsonPrimitive.boolean) {
                    return true
                }
            }
        }
        return false
    }

    override fun match(tokens: List<TokenInterface>): List<Throwable>? {
        val list = mutableListOf<Throwable>()
        for (i in 0 until tokens.size - 1) {
            if (tokens[i] is WhiteSpaceToken && tokens[i + 1] is WhiteSpaceToken) {
                list.add(MoreThanOneSpaceAfterTokenException("More than one space between tokens at index $i"))
            }
        }
        return list.toList()
    }

    override fun getRuleNameAndValue(): LinterDto =
        LinterDto(
            name = "one-space-between-tokens",
            data =
                listOf(
                    linter.dto.DataItem(
                        value = "activate",
                        default = "true",
                        type = "Boolean",
                    ),
                ),
        )
}
