package linter.rules.custom

import common.exception.NoNewLineAfterSemiColon
import common.token.EndSentenceToken
import common.token.NewLineToken
import common.token.abs.TokenInterface
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import linter.dto.LinterDto
import linter.rules.abs.LinterRule
import linter.rules.abs.RuleSettings

class LineJumpAfterSemicolonRule :
    LinterRule,
    RuleSettings {
    private var lines = 1 // default

    /*
      "line-breaks-before-semicolon": 2
     */

    override fun setRule(options: Map<String, JsonElement>) {
        lines = options["line-breaks-before-semicolon"]?.jsonPrimitive?.int ?: 1
    }

    override fun applies(rules: Map<String, JsonElement>): Boolean {
        for ((key, _) in rules) {
            if (key == "line-breaks-before-semicolon") {
                return true
            }
        }
        return false
    }

    override fun match(tokens: List<TokenInterface>): List<Throwable> {
        val list = mutableListOf<Throwable>()
        for ((index, token) in tokens.withIndex()) {
            if (token is EndSentenceToken) {
                var newLineCount = 0
                var currentIndex = index + 1

                // Count consecutive NewLineTokens after the semicolon
                while (currentIndex < tokens.size && tokens[currentIndex] is NewLineToken) {
                    newLineCount++
                    currentIndex++
                }

                // Check if we have the exact number of newlines required
                if (newLineCount != lines) {
                    list.add(NoNewLineAfterSemiColon(token.getPosition()))
                }
            }
        }
        return list.toList()
    }

    override fun getRuleNameAndValue(): LinterDto =
        LinterDto(
            name = "line-breaks-before-semicolon",
            data =
                listOf(
                    linter.dto.DataItem(
                        value = "number of lines breaks after semicolon",
                        default = "$lines",
                        type = "Number",
                    ),
                ),
        )
}
