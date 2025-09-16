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
import linter.rules.custom.SpaceBeforeAssignationRule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpaceBeforeAssignationRuleTest {
    @Test
    fun `applies should return true when space-before-assignation-rule is true`() {
        val rule = SpaceBeforeAssignationRule()
        val rules = mapOf("space-before-assignation-rule" to JsonPrimitive(true))

        val result = rule.applies(rules)
        assertTrue(result)
    }

    @Test
    fun `applies should return false when space-before-assignation-rule is false`() {
        val rule = SpaceBeforeAssignationRule()
        val rules = mapOf("space-before-assignation-rule" to JsonPrimitive(false))

        val result = rule.applies(rules)
        assertFalse(result)
    }

    @Test
    fun `applies should return false when space-before-assignation-rule key is not present`() {
        val rule = SpaceBeforeAssignationRule()
        val rules = mapOf("other-rule" to JsonPrimitive(true))

        val result = rule.applies(rules)
        assertFalse(result)
    }

    @Test
    fun `applies should return false when rules map is empty`() {
        val rule = SpaceBeforeAssignationRule()
        val rules = emptyMap<String, kotlinx.serialization.json.JsonElement>()

        val result = rule.applies(rules)
        assertFalse(result)
    }

    @Test
    fun `applies should handle multiple rules in map`() {
        val rule = SpaceBeforeAssignationRule()
        val rules =
            mapOf(
                "other-rule" to JsonPrimitive(false),
                "space-before-assignation-rule" to JsonPrimitive(true),
                "another-rule" to JsonPrimitive(true),
            )

        val result = rule.applies(rules)
        assertTrue(result)
    }

    @Test
    fun `SpaceBeforeAssignationRule should pass when space exists before equals`() {
        val rule = SpaceBeforeAssignationRule()
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
    fun `SpaceBeforeAssignationRule should fail when no space before equals`() {
        val rule = SpaceBeforeAssignationRule()
        val tokens =
            listOf(
                VariableToken("a", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2), // No space before equals
                WhiteSpaceToken(1, 3),
                NumberLiteralToken(5, 1, 4),
            ) // a= 5

        val result = rule.match(tokens)
        assertFalse(result.isEmpty())
    }

    @Test
    fun `SpaceBeforeAssignationRule should pass with multiple assignments having spaces before`() {
        val rule = SpaceBeforeAssignationRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                NumberLiteralToken(5, 1, 4),
                VariableToken("y", 1, 5),
                WhiteSpaceToken(1, 6),
                OperationToken(OperationEnum.EQUAL, 1, 7),
                NumberLiteralToken(10, 1, 8),
            ) // x =5 y =10

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceBeforeAssignationRule should fail with multiple assignments missing spaces before`() {
        val rule = SpaceBeforeAssignationRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2), // No space before first equals
                NumberLiteralToken(5, 1, 3),
                VariableToken("y", 1, 4),
                OperationToken(OperationEnum.EQUAL, 1, 5), // No space before second equals
                NumberLiteralToken(10, 1, 6),
            ) // x=5 y=10

        val result = rule.match(tokens)
        assertFalse(result.isEmpty())
    }

    @Test
    fun `SpaceBeforeAssignationRule should handle mixed scenarios correctly`() {
        val rule = SpaceBeforeAssignationRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3), // This assignment has space before
                NumberLiteralToken(5, 1, 4),
                VariableToken("y", 1, 5),
                OperationToken(OperationEnum.EQUAL, 1, 6), // This assignment doesn't have space before
                NumberLiteralToken(10, 1, 7),
            ) // x =5 y=10

        val result = rule.match(tokens)
        assertFalse(result.isEmpty()) // Should fail because second assignment has no space before
    }

    @Test
    fun `SpaceBeforeAssignationRule should ignore non-equal operators`() {
        val rule = SpaceBeforeAssignationRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.SUM, 1, 2), // No space before plus, but rule should ignore
                NumberLiteralToken(5, 1, 3),
                OperationToken(OperationEnum.MINUS, 1, 4), // No space before minus, but rule should ignore
                NumberLiteralToken(3, 1, 5),
            ) // x+5-3

        val result = rule.match(tokens)
        assertTrue(result.isEmpty()) // Should pass because rule only checks EQUAL operators
    }

    @Test
    fun `SpaceBeforeAssignationRule should pass when equals is first token`() {
        val rule = SpaceBeforeAssignationRule()
        val tokens =
            listOf(
                OperationToken(OperationEnum.EQUAL, 1, 1), // Equals is first token
                WhiteSpaceToken(1, 2),
                NumberLiteralToken(5, 1, 3),
            ) // =5

        val result = rule.match(tokens)
        assertFalse(result.isEmpty()) // Should fail because there's no token before equals (getOrNull returns null)
    }

    @Test
    fun `SpaceBeforeAssignationRule should pass with string assignments having spaces before`() {
        val rule = SpaceBeforeAssignationRule()
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
            ) // name : String =  "value"

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceBeforeAssignationRule should fail with string assignments missing spaces before`() {
        val rule = SpaceBeforeAssignationRule()
        val tokens =
            listOf(
                VariableToken("name", 1, 1),
                WhiteSpaceToken(1, 2),
                TypeDeclaratorToken(1, 3),
                WhiteSpaceToken(1, 4),
                TypeToken(TypeEnum.STRING, 1, 5),
                OperationToken(OperationEnum.EQUAL, 1, 6), // No space before equals
                WhiteSpaceToken(1, 7),
                StringLiteralToken("value", 1, 8),
            ) // name : String= "value"

        val result = rule.match(tokens)
        assertFalse(result.isEmpty())
    }

    @Test
    fun `SpaceBeforeAssignationRule should pass with function call assignments having spaces before`() {
        val rule = SpaceBeforeAssignationRule()
        val tokens =
            listOf(
                VariableToken("input", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                WhiteSpaceToken(1, 4),
                FunctionToken(FunctionEnum.PRINTLN, 1, 5),
            ) // input = println

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceBeforeAssignationRule should pass with assignment preceded by different token types`() {
        val rule = SpaceBeforeAssignationRule()
        val tokens =
            listOf(
                NumberLiteralToken(5, 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3), // Preceded by number
                WhiteSpaceToken(1, 4),
                StringLiteralToken("test", 1, 5),
                EndSentenceToken(1, 6),
                WhiteSpaceToken(1, 7),
                OperationToken(OperationEnum.EQUAL, 1, 8), // Preceded by semicolon + space
                NumberLiteralToken(10, 1, 9),
            )

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceBeforeAssignationRule should fail with assignment preceded by different token types without space`() {
        val rule = SpaceBeforeAssignationRule()
        val tokens =
            listOf(
                NumberLiteralToken(5, 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2), // No space before equals
                StringLiteralToken("test", 1, 3),
                EndSentenceToken(1, 4),
                OperationToken(OperationEnum.EQUAL, 1, 5), // No space before equals
                NumberLiteralToken(10, 1, 6),
            )

        val result = rule.match(tokens)
        assertFalse(result.isEmpty())
    }

    @Test
    fun `SpaceBeforeAssignationRule should pass with single token`() {
        val rule = SpaceBeforeAssignationRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
            )

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceBeforeAssignationRule should pass with empty token list`() {
        val rule = SpaceBeforeAssignationRule()
        val tokens = emptyList<common.token.abs.TokenInterface>()

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceBeforeAssignationRule should pass when no assignment operators present`() {
        val rule = SpaceBeforeAssignationRule()
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
}
