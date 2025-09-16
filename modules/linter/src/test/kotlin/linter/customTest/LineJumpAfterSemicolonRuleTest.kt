package linter.customTest

import common.exception.NoNewLineAfterSemiColon
import common.token.EndSentenceToken
import common.token.NewLineToken
import common.token.VariableToken
import kotlinx.serialization.json.JsonPrimitive
import linter.Linter
import linter.rules.custom.LineJumpAfterSemicolonRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LineJumpAfterSemicolonRuleTest {
    private lateinit var rule: LineJumpAfterSemicolonRule

    @BeforeEach
    fun setUp() {
        rule = LineJumpAfterSemicolonRule()
    }

    // Test applies() method
    @Test
    fun `applies should return true when line-breaks-before-semicolon key exists`() {
        val options = mapOf("line-breaks-before-semicolon" to JsonPrimitive(2))

        assertTrue(rule.applies(options))
    }

    @Test
    fun `applies should return false when line-breaks-before-semicolon key does not exist`() {
        val options = mapOf("other-rule" to JsonPrimitive(true))

        assertFalse(rule.applies(options))
    }

    @Test
    fun `applies should return false when options are empty`() {
        val options = emptyMap<String, kotlinx.serialization.json.JsonElement>()

        assertFalse(rule.applies(options))
    }

    // Test setRule() method
    @Test
    fun `setRule should set lines to specified value`() {
        val options = mapOf("line-breaks-before-semicolon" to JsonPrimitive(3))
        rule.setRule(options)

        val linter = Linter(listOf(rule))

        // Test by checking behavior - if lines is set to 3, we need exactly 3 newlines
        val tokens =
            listOf(
                VariableToken("test", 1, 1),
                EndSentenceToken(1, 2),
                NewLineToken(1, 3),
                NewLineToken(1, 4),
                NewLineToken(1, 5), // 3 newlines
                VariableToken("next", 2, 1),
            )

        val violations = linter.lint(tokens)
        assertTrue(violations.isEmpty()) // Should pass with exactly 3 newlines
    }

    @Test
    fun `setRule should default to 1 when key does not exist`() {
        val options = mapOf("other-rule" to JsonPrimitive(5))
        rule.setRule(options)

        val linter = Linter(listOf(rule))

        // Test default behavior - should require exactly 1 newline
        val tokens =
            listOf(
                VariableToken("test", 1, 1),
                EndSentenceToken(1, 2),
                NewLineToken(1, 3), // 1 newline (default)
                VariableToken("next", 2, 1),
            )

        val violations = linter.lint(tokens)
        assertTrue(violations.isEmpty()) // Should pass with exactly 1 newline
    }

    @Test
    fun `setRule should default to 1 when options are empty`() {
        val options = emptyMap<String, kotlinx.serialization.json.JsonElement>()
        rule.setRule(options)

        val linter = Linter(listOf(rule))

        // Test default behavior
        val tokens =
            listOf(
                EndSentenceToken(1, 1),
                NewLineToken(1, 2), // 1 newline (default)
            )

        val violations = linter.lint(tokens)
        assertTrue(violations.isEmpty())
    }

    // Test through Linter with default setting (1 newline)
    @Test
    fun `linter should pass when exactly 1 newline follows semicolon (default)`() {
        val linter = Linter(listOf(rule))

        val tokens =
            listOf(
                VariableToken("test", 1, 1),
                EndSentenceToken(1, 2),
                NewLineToken(1, 3), // Exactly 1 newline
                VariableToken("next", 2, 1),
            )

        val violations = linter.lint(tokens)
        assertTrue(violations.isEmpty())
    }

    @Test
    fun `linter should fail when no newline follows semicolon (default)`() {
        val linter = Linter(listOf(rule))

        val tokens =
            listOf(
                VariableToken("test", 1, 1),
                EndSentenceToken(1, 2),
                VariableToken("next", 1, 3), // No newline
            )

        val violations = linter.lint(tokens)
        assertEquals(1, violations.size)
        assertTrue(violations[0] is NoNewLineAfterSemiColon)
    }

    @Test
    fun `linter should fail when more than 1 newline follows semicolon (default)`() {
        val linter = Linter(listOf(rule))

        val tokens =
            listOf(
                VariableToken("test", 1, 1),
                EndSentenceToken(1, 2),
                NewLineToken(1, 3),
                NewLineToken(1, 4), // 2 newlines, but default expects 1
                VariableToken("next", 2, 1),
            )

        val violations = linter.lint(tokens)
        assertEquals(1, violations.size)
        assertTrue(violations[0] is NoNewLineAfterSemiColon)
    }

    // Test through Linter with custom settings
    @Test
    fun `linter should pass when exactly 2 newlines follow semicolon when configured`() {
        val options = mapOf("line-breaks-before-semicolon" to JsonPrimitive(2))
        rule.setRule(options)

        val linter = Linter(listOf(rule))

        val tokens =
            listOf(
                VariableToken("test", 1, 1),
                EndSentenceToken(1, 2),
                NewLineToken(1, 3),
                NewLineToken(1, 4), // Exactly 2 newlines
                VariableToken("next", 3, 1),
            )

        val violations = linter.lint(tokens)
        assertTrue(violations.isEmpty())
    }

    @Test
    fun `linter should fail when 1 newline but 2 expected`() {
        val options = mapOf("line-breaks-before-semicolon" to JsonPrimitive(2))
        rule.setRule(options)

        val linter = Linter(listOf(rule))

        val tokens =
            listOf(
                VariableToken("test", 1, 1),
                EndSentenceToken(1, 2),
                NewLineToken(1, 3), // Only 1 newline, but 2 expected
                VariableToken("next", 2, 1),
            )

        val violations = linter.lint(tokens)
        assertEquals(1, violations.size)
        assertTrue(violations[0] is NoNewLineAfterSemiColon)
    }

    @Test
    fun `linter should pass when 0 newlines expected and configured`() {
        val options = mapOf("line-breaks-before-semicolon" to JsonPrimitive(0))
        rule.setRule(options)

        val linter = Linter(listOf(rule))

        val tokens =
            listOf(
                VariableToken("test", 1, 1),
                EndSentenceToken(1, 2),
                VariableToken("next", 1, 3), // No newline, and 0 expected
            )

        val violations = linter.lint(tokens)
        assertTrue(violations.isEmpty())
    }
}
