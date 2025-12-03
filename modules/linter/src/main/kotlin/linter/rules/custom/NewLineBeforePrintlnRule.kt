package linter.rules.custom

import common.enums.FunctionEnum
import common.exception.InvalidNewLineBeforePrintlnException
import common.token.FunctionToken
import common.token.NewLineToken
import common.token.abs.TokenInterface
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import linter.dto.LinterDto
import linter.rules.abs.LinterRule
import linter.rules.abs.RuleSettings

class NewLineBeforePrintlnRule :
    LinterRule,
    RuleSettings {
    private var allowedNewLines = 1 // default

    /*
       "new-line-before-println": 1
     */

    override fun setRule(options: Map<String, JsonElement>) {
        allowedNewLines = options["new-line-before-println"]?.jsonPrimitive?.int ?: 1
    }

    override fun applies(rules: Map<String, JsonElement>): Boolean = rules.containsKey("new-line-before-println")

    override fun match(tokens: List<TokenInterface>): List<Throwable> {
        val list = mutableListOf<Throwable>()
        for ((index, token) in tokens.withIndex()) {
            if (token is FunctionToken && token.value == FunctionEnum.PRINTLN) {
                val newLines = countConsecutiveNewLines(tokens, index - 1)
                if (newLines != allowedNewLines) {
                    list.add(InvalidNewLineBeforePrintlnException(expected = allowedNewLines, found = newLines))
                }
            }
        }
        return list.toList()
    }

    private fun countConsecutiveNewLines(
        tokens: List<TokenInterface>,
        startIndex: Int,
    ): Int {
        var count = 0
        var i = startIndex
        while (i >= 0 && tokens[i] is NewLineToken) {
            count++
            i--
        }
        return count
    }

    override fun getRuleNameAndValue(): LinterDto =
        LinterDto(
            name = "new-line-before-println",
            data =
                listOf(
                    linter.dto.DataItem(
                        value = "number of new lines required before a println statement",
                        default = "$allowedNewLines",
                        type = "Number",
                    ),
                ),
        )
}
