package linter.customTest

import common.enums.FunctionEnum
import common.enums.OperationEnum
import common.enums.TypeEnum
import common.token.EndSentenceToken
import common.token.FunctionToken
import common.token.NumberLiteralToken
import common.token.OperationToken
import common.token.StringLiteralToken
import common.token.TypeDeclaratorToken
import common.token.TypeToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import kotlinx.serialization.json.JsonPrimitive
import linter.rules.custom.SpaceAfterAssignationRule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpaceAfterAssignationRuleTest {
    @Test
    fun `applies should return true when one-space-between-tokens is true`() {
        val rule = SpaceAfterAssignationRule()
        val rules = mapOf("one-space-between-tokens" to JsonPrimitive(true))

        val result = rule.applies(rules)
        assertTrue(result)
    }

    @Test
    fun `applies should return false when one-space-between-tokens is false`() {
        val rule = SpaceAfterAssignationRule()
        val rules = mapOf("one-space-between-tokens" to JsonPrimitive(false))

        val result = rule.applies(rules)
        assertFalse(result)
    }

    @Test
    fun `applies should return false when one-space-between-tokens key is not present`() {
        val rule = SpaceAfterAssignationRule()
        val rules = mapOf("other-rule" to JsonPrimitive(true))

        val result = rule.applies(rules)
        assertFalse(result)
    }

    @Test
    fun `applies should return false when rules map is empty`() {
        val rule = SpaceAfterAssignationRule()
        val rules = emptyMap<String, kotlinx.serialization.json.JsonElement>()

        val result = rule.applies(rules)
        assertFalse(result)
    }

    @Test
    fun `applies should handle multiple rules in map`() {
        val rule = SpaceAfterAssignationRule()
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
    fun `SpaceAfterAssignationRule should pass when space exists after equals`() {
        val rule = SpaceAfterAssignationRule()
        val tokens =
            listOf(
                VariableToken("a", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                WhiteSpaceToken(1, 4),
                NumberLiteralToken(5, 1, 5),
            ) // a = 5

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceAfterAssignationRule should fail when no space after equals`() {
        val rule = SpaceAfterAssignationRule()
        val tokens =
            listOf(
                VariableToken("a", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                NumberLiteralToken(5, 1, 4), // No space after equals
            ) // a =5

        val result = rule.match(tokens)
        assertFalse(result.isEmpty())
    }

    @Test
    fun `SpaceAfterAssignationRule should pass with multiple assignments having spaces`() {
        val rule = SpaceAfterAssignationRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                WhiteSpaceToken(1, 3),
                NumberLiteralToken(5, 1, 4),
                VariableToken("y", 1, 5),
                OperationToken(OperationEnum.EQUAL, 1, 6),
                WhiteSpaceToken(1, 7),
                NumberLiteralToken(10, 1, 8),
            ) // x= 5 y= 10

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceAfterAssignationRule should fail with multiple assignments missing spaces`() {
        val rule = SpaceAfterAssignationRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                NumberLiteralToken(5, 1, 3), // No space after first equals
                VariableToken("y", 1, 4),
                OperationToken(OperationEnum.EQUAL, 1, 5),
                NumberLiteralToken(10, 1, 6), // No space after second equals
            ) // x=5 y=10

        val result = rule.match(tokens)
        assertFalse(result.isEmpty())
    }

    @Test
    fun `SpaceAfterAssignationRule should handle mixed scenarios correctly`() {
        val rule = SpaceAfterAssignationRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                WhiteSpaceToken(1, 3), // This assignment has space
                NumberLiteralToken(5, 1, 4),
                VariableToken("y", 1, 5),
                OperationToken(OperationEnum.EQUAL, 1, 6),
                NumberLiteralToken(10, 1, 7), // This assignment doesn't have space
            ) // x= 5 y=10

        val result = rule.match(tokens)
        assertFalse(result.isEmpty()) // Should fail because second assignment has no space
    }

    @Test
    fun `SpaceAfterAssignationRule should ignore non-equal operators`() {
        val rule = SpaceAfterAssignationRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.SUM, 1, 2),
                NumberLiteralToken(5, 1, 3), // No space after plus, but rule should ignore
                OperationToken(OperationEnum.MINUS, 1, 4),
                NumberLiteralToken(3, 1, 5), // No space after minus, but rule should ignore
            ) // x+5-3

        val result = rule.match(tokens)
        assertTrue(result.isEmpty()) // Should pass because rule only checks EQUAL operators
    }

    @Test
    fun `SpaceAfterAssignationRule should pass when equals is last token`() {
        val rule = SpaceAfterAssignationRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3), // Equals is last token
            )

        val result = rule.match(tokens)
        assertFalse(result.isEmpty()) // Should fail because there's no token after equals (getOrNull returns null)
    }

    @Test
    fun `SpaceAfterAssignationRule should pass with string assignments having spaces`() {
        val rule = SpaceAfterAssignationRule()
        val tokens =
            listOf(
                VariableToken("name", 1, 1),
                WhiteSpaceToken(1, 2),
                TypeDeclaratorToken(1, 3),
                WhiteSpaceToken(1, 4),
                TypeToken(TypeEnum.STRING, 1, 5),
                OperationToken(OperationEnum.EQUAL, 1, 6),
                WhiteSpaceToken(1, 7),
                StringLiteralToken("value", 1, 8),
            ) // name : String= "value"

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceAfterAssignationRule should fail with string assignments missing spaces`() {
        val rule = SpaceAfterAssignationRule()
        val tokens =
            listOf(
                VariableToken("name", 1, 1),
                WhiteSpaceToken(1, 2),
                TypeDeclaratorToken(1, 3),
                WhiteSpaceToken(1, 4),
                TypeToken(TypeEnum.STRING, 1, 5),
                OperationToken(OperationEnum.EQUAL, 1, 6),
                StringLiteralToken("value", 1, 7), // No space after equals
            ) // name : String="value"

        val result = rule.match(tokens)
        assertFalse(result.isEmpty())
    }

    @Test
    fun `SpaceAfterAssignationRule should pass with function call assignments having spaces`() {
        val rule = SpaceAfterAssignationRule()
        val tokens =
            listOf(
                VariableToken("input", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                WhiteSpaceToken(1, 3),
                FunctionToken(FunctionEnum.PRINTLN, 1, 4),
            ) // input= println

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceAfterAssignationRule should pass with single token`() {
        val rule = SpaceAfterAssignationRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
            )

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceAfterAssignationRule should pass with empty token list`() {
        val rule = SpaceAfterAssignationRule()
        val tokens = emptyList<common.token.abs.TokenInterface>()

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceAfterAssignationRule should pass when no assignment operators present`() {
        val rule = SpaceAfterAssignationRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.SUM, 1, 3),
                WhiteSpaceToken(1, 4),
                NumberLiteralToken(5, 1, 5),
            ) // x + 5 (no assignment)

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceAfterAssignationRule should handle assignment followed by end sentence token`() {
        val rule = SpaceAfterAssignationRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                WhiteSpaceToken(1, 3),
                NumberLiteralToken(5, 1, 4),
                EndSentenceToken(1, 5),
            ) // x= 5;

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }
}
