package linter.customTest

import common.enums.FunctionEnum
import common.enums.OperationEnum
import common.enums.TypeEnum
import common.token.FunctionToken
import common.token.NumberLiteralToken
import common.token.OperationToken
import common.token.StringLiteralToken
import common.token.TypeDeclaratorToken
import common.token.TypeToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import kotlinx.serialization.json.JsonPrimitive
import linter.rules.custom.OneSpaceBetweenTokensRule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OneSpaceBetweenTokensRuleTest {
    @Test
    fun `applies should return true when one-space-between-tokens is true`() {
        val rule = OneSpaceBetweenTokensRule()
        val rules = mapOf("one-space-between-tokens" to JsonPrimitive(true))

        val result = rule.applies(rules)
        assertTrue(result)
    }

    @Test
    fun `applies should return false when one-space-between-tokens is false`() {
        val rule = OneSpaceBetweenTokensRule()
        val rules = mapOf("one-space-between-tokens" to JsonPrimitive(false))

        val result = rule.applies(rules)
        assertFalse(result)
    }

    @Test
    fun `applies should return false when one-space-between-tokens key is not present`() {
        val rule = OneSpaceBetweenTokensRule()
        val rules = mapOf("other-rule" to JsonPrimitive(true))

        val result = rule.applies(rules)
        assertFalse(result)
    }

    @Test
    fun `applies should return false when rules map is empty`() {
        val rule = OneSpaceBetweenTokensRule()
        val rules = emptyMap<String, kotlinx.serialization.json.JsonElement>()

        val result = rule.applies(rules)
        assertFalse(result)
    }

    @Test
    fun `applies should handle multiple rules in map`() {
        val rule = OneSpaceBetweenTokensRule()
        val rules =
            mapOf(
                "other-rule" to JsonPrimitive(false),
                "one-space-between-tokens" to JsonPrimitive(true),
                "another-rule" to JsonPrimitive(true),
            )

        val result = rule.applies(rules)
        assertTrue(result)
    }

    @Test
    fun `OneSpaceBetweenTokensRule should pass when single spaces exist between tokens`() {
        val rule = OneSpaceBetweenTokensRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                WhiteSpaceToken(1, 4),
                NumberLiteralToken(5, 1, 5),
            )

        val result = rule.match(tokens)
        assertTrue(result?.isEmpty() == true)
    }

    @Test
    fun `OneSpaceBetweenTokensRule should fail when consecutive whitespace tokens exist`() {
        val rule = OneSpaceBetweenTokensRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                WhiteSpaceToken(1, 3), // Two consecutive whitespace tokens
                OperationToken(OperationEnum.EQUAL, 1, 4),
                NumberLiteralToken(5, 1, 5),
            )

        val result = rule.match(tokens)
        assertFalse(result?.isEmpty() == true)
    }

    @Test
    fun `OneSpaceBetweenTokensRule should fail when multiple consecutive whitespace tokens exist`() {
        val rule = OneSpaceBetweenTokensRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                WhiteSpaceToken(1, 3),
                WhiteSpaceToken(1, 4), // Three consecutive whitespace tokens
                OperationToken(OperationEnum.EQUAL, 1, 5),
                NumberLiteralToken(5, 1, 6),
            )

        val result = rule.match(tokens)
        assertFalse(result?.isEmpty() == true)
    }

    @Test
    fun `OneSpaceBetweenTokensRule should pass when no whitespace tokens exist`() {
        val rule = OneSpaceBetweenTokensRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                NumberLiteralToken(5, 1, 3),
            )

        val result = rule.match(tokens)
        assertTrue(result?.isEmpty() == true)
    }

    @Test
    fun `OneSpaceBetweenTokensRule should pass with complex token sequence with single spaces`() {
        val rule = OneSpaceBetweenTokensRule()
        val tokens =
            listOf(
                VariableToken("name", 1, 1),
                WhiteSpaceToken(1, 2),
                TypeDeclaratorToken(1, 3),
                WhiteSpaceToken(1, 4),
                TypeToken(TypeEnum.STRING, 1, 5),
                WhiteSpaceToken(1, 6),
                OperationToken(OperationEnum.EQUAL, 1, 7),
                WhiteSpaceToken(1, 8),
                StringLiteralToken("value", 1, 9),
            ) // name : String = "value"

        val result = rule.match(tokens)
        assertTrue(result?.isEmpty() == true)
    }

    @Test
    fun `OneSpaceBetweenTokensRule should fail with multiple violations in same token list`() {
        val rule = OneSpaceBetweenTokensRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                WhiteSpaceToken(1, 3), // First violation
                OperationToken(OperationEnum.EQUAL, 1, 4),
                WhiteSpaceToken(1, 5),
                WhiteSpaceToken(1, 6), // Second violation
                NumberLiteralToken(5, 1, 7),
            )

        val result = rule.match(tokens)
        assertFalse(result?.isEmpty() == true)
    }

    @Test
    fun `OneSpaceBetweenTokensRule should pass with function tokens and single spaces`() {
        val rule = OneSpaceBetweenTokensRule()
        val tokens =
            listOf(
                FunctionToken(FunctionEnum.PRINTLN, 1, 1),
                WhiteSpaceToken(1, 2),
                StringLiteralToken("Hello", 1, 3),
            )

        val result = rule.match(tokens)
        assertTrue(result?.isEmpty() == true)
    }

    @Test
    fun `OneSpaceBetweenTokensRule should fail with function tokens and multiple spaces`() {
        val rule = OneSpaceBetweenTokensRule()
        val tokens =
            listOf(
                FunctionToken(FunctionEnum.PRINTLN, 1, 1),
                WhiteSpaceToken(1, 2),
                WhiteSpaceToken(1, 3), // Multiple spaces
                StringLiteralToken("Hello", 1, 4),
            )

        val result = rule.match(tokens)
        assertFalse(result?.isEmpty() == true)
    }

    @Test
    fun `OneSpaceBetweenTokensRule should pass with single token`() {
        val rule = OneSpaceBetweenTokensRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
            )

        val result = rule.match(tokens)
        assertTrue(result?.isEmpty() == true)
    }

    @Test
    fun `OneSpaceBetweenTokensRule should pass with empty token list`() {
        val rule = OneSpaceBetweenTokensRule()
        val tokens = emptyList<common.token.abs.TokenInterface>()

        val result = rule.match(tokens)
        assertTrue(result?.isEmpty() == true)
    }

    @Test
    fun `OneSpaceBetweenTokensRule should pass with non-adjacent whitespace tokens`() {
        val rule = OneSpaceBetweenTokensRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                WhiteSpaceToken(1, 4),
                NumberLiteralToken(5, 1, 5),
                WhiteSpaceToken(1, 6),
                VariableToken("y", 1, 7),
            ) // x = 5 y (whitespace tokens are separated by other tokens)

        val result = rule.match(tokens)
        assertTrue(result?.isEmpty() == true)
    }
}
