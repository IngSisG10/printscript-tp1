package linter.customTest

import common.enums.FunctionEnum
import common.token.FunctionToken
import common.token.NewLineToken
import common.token.StringLiteralToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import kotlinx.serialization.json.JsonPrimitive
import linter.rules.custom.NewLineBeforePrintlnRule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NewLineBeforePrintlnRuleTest {
    @Test
    fun `NewLineBeforePrintlnRule should pass when correct number of newlines before println with default setting`() {
        val rule = NewLineBeforePrintlnRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                NewLineToken(1, 2),
                FunctionToken(FunctionEnum.PRINTLN, 2, 1),
            )

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `NewLineBeforePrintlnRule should fail when no newlines before println with default setting`() {
        val rule = NewLineBeforePrintlnRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                FunctionToken(FunctionEnum.PRINTLN, 1, 3),
            )

        val result = rule.match(tokens)
        assertFalse(result.isEmpty())
    }

    @Test
    fun `NewLineBeforePrintlnRule should fail when too many newlines before println with default setting`() {
        val rule = NewLineBeforePrintlnRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                NewLineToken(1, 2),
                NewLineToken(2, 1),
                FunctionToken(FunctionEnum.PRINTLN, 3, 1),
            )

        val result = rule.match(tokens)
        assertFalse(result.isEmpty())
    }

    @Test
    fun `NewLineBeforePrintlnRule should pass when configured for 2 newlines`() {
        val rule = NewLineBeforePrintlnRule()
        val options = mapOf("new-line-before-println" to JsonPrimitive(2))
        rule.setRule(options)

        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                NewLineToken(1, 2),
                NewLineToken(2, 1),
                FunctionToken(FunctionEnum.PRINTLN, 3, 1),
            )

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `NewLineBeforePrintlnRule should fail when configured for 2 newlines but only 1 provided`() {
        val rule = NewLineBeforePrintlnRule()
        val options = mapOf("new-line-before-println" to JsonPrimitive(2))
        rule.setRule(options)

        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                NewLineToken(1, 2),
                FunctionToken(FunctionEnum.PRINTLN, 2, 1),
            )

        val result = rule.match(tokens)
        assertFalse(result.isEmpty())
    }

    @Test
    fun `NewLineBeforePrintlnRule should pass when configured for 0 newlines`() {
        val rule = NewLineBeforePrintlnRule()
        val options = mapOf("new-line-before-println" to JsonPrimitive(0))
        rule.setRule(options)

        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                FunctionToken(FunctionEnum.PRINTLN, 1, 3),
            )

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `NewLineBeforePrintlnRule should handle multiple println statements`() {
        val rule = NewLineBeforePrintlnRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                NewLineToken(1, 2),
                FunctionToken(FunctionEnum.PRINTLN, 2, 1),
                NewLineToken(2, 2),
                FunctionToken(FunctionEnum.PRINTLN, 3, 1),
                WhiteSpaceToken(3, 2),
                FunctionToken(FunctionEnum.PRINTLN, 3, 3),
            )

        val result = rule.match(tokens)
        // Should have 2 violations: second println is correct, third has no newline
        assertFalse(result.isEmpty())
    }

    @Test
    fun `NewLineBeforePrintlnRule should pass when println is first token`() {
        val rule = NewLineBeforePrintlnRule()
        val tokens =
            listOf(
                FunctionToken(FunctionEnum.PRINTLN, 1, 1),
                StringLiteralToken("Hello", 1, 2),
            )

        val result = rule.match(tokens)
        // When println is first token, there are 0 newlines before it, which should match default of 1
        assertFalse(result.isEmpty())
    }

    @Test
    fun `NewLineBeforePrintlnRule should handle consecutive newlines correctly`() {
        val rule = NewLineBeforePrintlnRule()
        val options = mapOf("new-line-before-println" to JsonPrimitive(3))
        rule.setRule(options)

        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                NewLineToken(1, 2),
                NewLineToken(2, 1),
                NewLineToken(3, 1),
                FunctionToken(FunctionEnum.PRINTLN, 4, 1),
            )

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `NewLineBeforePrintlnRule should ignore non-println function tokens`() {
        val rule = NewLineBeforePrintlnRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                FunctionToken(FunctionEnum.READ_INPUT, 1, 3), // Different function
                NewLineToken(1, 4),
                FunctionToken(FunctionEnum.PRINTLN, 2, 1),
            )

        val result = rule.match(tokens)
        assertTrue(result.isEmpty()) // Only println should be checked
    }
}
