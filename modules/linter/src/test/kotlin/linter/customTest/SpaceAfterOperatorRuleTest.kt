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
import linter.rules.custom.SpaceAfterOperatorRule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpaceAfterOperatorRuleTest {
    @Test
    fun `SpaceAfterOperatorRule should pass when space exists after operator`() {
        val rule = SpaceAfterOperatorRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                WhiteSpaceToken(1, 3),
                NumberLiteralToken(5, 1, 4),
            )

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceAfterOperatorRule should fail when no space after operator`() {
        val rule = SpaceAfterOperatorRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                NumberLiteralToken(5, 1, 3), // No space after operator
            )

        val result = rule.match(tokens)
        assertFalse(result.isEmpty())
    }

    @Test
    fun `SpaceAfterOperatorRule should pass with multiple operators and spaces`() {
        val rule = SpaceAfterOperatorRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                WhiteSpaceToken(1, 3),
                VariableToken("y", 1, 4),
                OperationToken(OperationEnum.SUM, 1, 5),
                WhiteSpaceToken(1, 6),
                NumberLiteralToken(10, 1, 7),
            ) // x = y + 10

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceAfterOperatorRule should fail with multiple operators missing spaces`() {
        val rule = SpaceAfterOperatorRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                VariableToken("y", 1, 3), // No space after first operator
                OperationToken(OperationEnum.SUM, 1, 4),
                NumberLiteralToken(10, 1, 5), // No space after second operator
            ) // x=y+10

        val result = rule.match(tokens)
        assertFalse(result.isEmpty())
    }

    @Test
    fun `SpaceAfterOperatorRule should pass with different operation types`() {
        val rule = SpaceAfterOperatorRule()
        val tokens =
            listOf(
                VariableToken("a", 1, 1),
                OperationToken(OperationEnum.MINUS, 1, 2),
                WhiteSpaceToken(1, 3),
                VariableToken("b", 1, 4),
                OperationToken(OperationEnum.MULTIPLY, 1, 5),
                WhiteSpaceToken(1, 6),
                VariableToken("c", 1, 7),
                OperationToken(OperationEnum.DIVIDE, 1, 8),
                WhiteSpaceToken(1, 9),
                NumberLiteralToken(2, 1, 10),
            ) // a - b * c / 2

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceAfterOperatorRule should fail with different operation types without spaces`() {
        val rule = SpaceAfterOperatorRule()
        val tokens =
            listOf(
                VariableToken("a", 1, 1),
                OperationToken(OperationEnum.MINUS, 1, 2),
                VariableToken("b", 1, 3), // No space
                OperationToken(OperationEnum.MULTIPLY, 1, 4),
                VariableToken("c", 1, 5), // No space
            ) // a-b*c

        val result = rule.match(tokens)
        assertFalse(result.isEmpty())
    }

    @Test
    fun `SpaceAfterOperatorRule should pass when operator is last token`() {
        val rule = SpaceAfterOperatorRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3), // Operator is last token
            )

        val result = rule.match(tokens)
        assertTrue(result.isEmpty()) // No violation since there's no token after the operator
    }

    @Test
    fun `SpaceAfterOperatorRule should pass with mixed token types and proper spacing`() {
        val rule = SpaceAfterOperatorRule()
        val tokens =
            listOf(
                VariableToken("name", 1, 1),
                TypeDeclaratorToken(1, 2),
                TypeToken(TypeEnum.STRING, 1, 3),
                OperationToken(OperationEnum.EQUAL, 1, 4),
                WhiteSpaceToken(1, 5),
                StringLiteralToken("value", 1, 6),
            ) // name:String= "value"

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceAfterOperatorRule should fail with mixed token types without spacing`() {
        val rule = SpaceAfterOperatorRule()
        val tokens =
            listOf(
                VariableToken("name", 1, 1),
                TypeDeclaratorToken(1, 2),
                TypeToken(TypeEnum.STRING, 1, 3),
                OperationToken(OperationEnum.EQUAL, 1, 4),
                StringLiteralToken("value", 1, 5), // No space after operator
            ) // name:String="value"

        val result = rule.match(tokens)
        assertFalse(result.isEmpty())
    }

    @Test
    fun `SpaceAfterOperatorRule should pass with single token`() {
        val rule = SpaceAfterOperatorRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
            )

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceAfterOperatorRule should pass with empty token list`() {
        val rule = SpaceAfterOperatorRule()
        val tokens = emptyList<common.token.abs.TokenInterface>()

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceAfterOperatorRule should pass with no operators in token list`() {
        val rule = SpaceAfterOperatorRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                StringLiteralToken("hello", 1, 3),
                WhiteSpaceToken(1, 4),
                NumberLiteralToken(42, 1, 5),
            )

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `SpaceAfterOperatorRule should handle operators followed by different token types`() {
        val rule = SpaceAfterOperatorRule()
        val tokens =
            listOf(
                VariableToken("result", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                WhiteSpaceToken(1, 3),
                FunctionToken(FunctionEnum.READ_INPUT, 1, 4),
                OperationToken(OperationEnum.SUM, 1, 5),
                WhiteSpaceToken(1, 6),
                EndSentenceToken(1, 7),
            )

        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }
}
