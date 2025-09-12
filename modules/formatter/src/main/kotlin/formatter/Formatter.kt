package formatter

import common.converter.Converter
import common.token.abs.TokenInterface
import formatter.fixes.IfBracePlacementFix
import formatter.fixes.LineJumpAfterSemiColonFix
import formatter.fixes.LineJumpSpaceBeforePrintlnFix
import formatter.fixes.MaxOneBlankLineFix
import formatter.fixes.OneSpaceAfterTokenMaxFix
import formatter.fixes.SpaceAfterColonFix
import formatter.fixes.SpaceBeforeAndAfterEqualFix
import formatter.fixes.SpaceBeforeAndAfterOperatorFix
import formatter.fixes.SpaceBeforeColonFix
import formatter.fixes.abs.FormatterFix

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
            IfBracePlacementFix(),
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
