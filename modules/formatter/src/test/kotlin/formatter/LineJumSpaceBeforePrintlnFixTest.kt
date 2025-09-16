package formatter

import common.enums.FunctionEnum
import common.token.EndSentenceToken
import common.token.FunctionToken
import common.token.NewLineToken
import common.token.VariableToken
import formatter.fixes.required.LineJumpSpaceBeforePrintlnFix
import kotlinx.serialization.json.JsonPrimitive
import org.junit.jupiter.api.Assertions.assertFalse
import kotlin.test.Test
import kotlin.test.assertEquals

class LineJumSpaceBeforePrintlnFixTest {
    @Test
    fun `line jump space before println fix adds one newline after println by default`() {
        val tokens =
            listOf(
                FunctionToken(FunctionEnum.PRINTLN, 1, 1),
                EndSentenceToken(1, 2),
                VariableToken("x", 1, 3),
            )

        val formatter = Formatter(listOf(LineJumpSpaceBeforePrintlnFix()))
        val result = formatter.format(tokens)

        // Should add 2 newlines (maxNewLines + 1 = 1 + 1 = 2)
        assertEquals("println;\n\nx", result)
    }

    @Test
    fun `line jump space before println fix adds specified number of newlines`() {
        val tokens =
            listOf(
                FunctionToken(FunctionEnum.PRINTLN, 1, 1),
                EndSentenceToken(1, 2),
                VariableToken("x", 1, 3),
            )

        // Configure to add 3 newlines after println (maxNewLines = 3, so 3+1 = 4 total)
        val fix = LineJumpSpaceBeforePrintlnFix()
        val configMap = mapOf("line-breaks-after-println" to JsonPrimitive(3))
        fix.setFix(configMap)

        val formatter = Formatter(listOf(fix))
        val result = formatter.format(tokens)

        assertEquals("println;\n\n\n\nx", result)
    }

    @Test
    fun `line jump space before println fix removes existing newlines after println`() {
        val tokens =
            listOf(
                FunctionToken(FunctionEnum.PRINTLN, 1, 1),
                EndSentenceToken(1, 2),
                NewLineToken(1, 3),
                NewLineToken(1, 4),
                NewLineToken(1, 5),
                VariableToken("x", 1, 6),
            )

        val formatter = Formatter(listOf(LineJumpSpaceBeforePrintlnFix()))
        val result = formatter.format(tokens)

        // Should remove existing newlines and add exactly 2 (1+1)
        assertEquals("println;\n\nx", result)
    }

    @Test
    fun `line jump space before println fix handles multiple println statements`() {
        val tokens =
            listOf(
                FunctionToken(FunctionEnum.PRINTLN, 1, 1),
                EndSentenceToken(1, 2),
                VariableToken("x", 1, 3),
                FunctionToken(FunctionEnum.PRINTLN, 1, 4),
                EndSentenceToken(1, 5),
                VariableToken("y", 1, 6),
            )

        val formatter = Formatter(listOf(LineJumpSpaceBeforePrintlnFix()))
        val result = formatter.format(tokens)

        assertEquals("println;\n\nxprintln;\n\ny", result)
    }

    @Test
    fun `line jump space before println fix adds zero newlines when configured to zero`() {
        val tokens =
            listOf(
                FunctionToken(FunctionEnum.PRINTLN, 1, 1),
                EndSentenceToken(1, 2),
                VariableToken("x", 1, 3),
            )

        val fix = LineJumpSpaceBeforePrintlnFix()
        val configMap = mapOf("line-breaks-after-println" to JsonPrimitive(0))
        fix.setFix(configMap)

        val formatter = Formatter(listOf(fix))
        val result = formatter.format(tokens)

        // Should add 0+1 = 1 newline
        assertEquals("println;\nx", result)
    }

    @Test
    fun `line jump space before println fix handles println without semicolon`() {
        val tokens =
            listOf(
                FunctionToken(FunctionEnum.PRINTLN, 1, 1),
                VariableToken("x", 1, 2),
            )

        val formatter = Formatter(listOf(LineJumpSpaceBeforePrintlnFix()))
        val result = formatter.format(tokens)

        // No semicolon means isNewLine never becomes true, so no newlines added
        assertEquals("printlnx", result)
    }

    @Test
    fun `line jump space before println fix only applies when configuration is valid`() {
        val tokens =
            listOf(
                FunctionToken(FunctionEnum.PRINTLN, 1, 1),
                EndSentenceToken(1, 2),
                VariableToken("x", 1, 3),
            )

        // Invalid configuration (negative value)
        val configMap = mapOf("line-breaks-after-println" to JsonPrimitive(-1))
        val fix = LineJumpSpaceBeforePrintlnFix()

        // applies() should return false for negative values
        assertFalse(fix.applies(configMap))
    }

    @Test
    fun `line jump space before println fix handles non-println functions normally`() {
        val tokens =
            listOf(
                FunctionToken(FunctionEnum.READ_INPUT, 1, 1), // Not println
                EndSentenceToken(1, 2),
                VariableToken("x", 1, 3),
            )

        val formatter = Formatter(listOf(LineJumpSpaceBeforePrintlnFix()))
        val result = formatter.format(tokens)

        // Should not add extra newlines for non-println functions
        assertEquals("readInput;x", result)
    }
}
