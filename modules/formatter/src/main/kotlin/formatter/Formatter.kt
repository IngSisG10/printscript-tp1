package formatter

import common.converter.Converter
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix
import formatter.fixes.custom.MaxOneBlankLineFix
import formatter.fixes.required.IfBraceSameLinePlacementFix
import formatter.fixes.required.LineJumpAfterSemiColonFix
import formatter.fixes.required.LineJumpSpaceBeforePrintlnFix
import formatter.fixes.required.OneSpaceAfterTokenMaxFix
import formatter.fixes.required.SpaceAfterColonFix
import formatter.fixes.required.SpaceBeforeAndAfterEqualFix
import formatter.fixes.required.SpaceBeforeAndAfterOperatorFix
import formatter.fixes.required.SpaceBeforeColonFix

class Formatter(
    private val formatterFixes: List<FormatterFix> =
        listOf(
            SpaceBeforeColonFix(),
            SpaceAfterColonFix(),
            OneSpaceAfterTokenMaxFix(),
            SpaceBeforeAndAfterEqualFix(),
            SpaceBeforeAndAfterOperatorFix(),
            LineJumpAfterSemiColonFix(),
            LineJumpSpaceBeforePrintlnFix(),
            MaxOneBlankLineFix(),
            IfBraceSameLinePlacementFix(),
        ),
) {
    private val converter = Converter()

    fun format(tokens: List<TokenInterface>): String {
        if (formatterFixes.isEmpty()) return converter.convert(tokens)
        if (tokens.isEmpty()) return ""
        var newTokenList: List<TokenInterface> = tokens
        for (fixData in formatterFixes) {
            newTokenList = fixData.fix(newTokenList)
        }
        return converter.convert(newTokenList)
    }
}
