package formatter

import common.enums.OperationEnum
import common.token.NumberLiteralToken
import common.token.OperationToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.required.SpaceBeforeAndAfterOperatorFix
import kotlinx.serialization.json.JsonPrimitive
import org.junit.jupiter.api.Assertions.assertFalse
import kotlin.test.Test
import kotlin.test.assertEquals

class SpaceBeforeAndAfterOperatorFixTest {
    @Test
    fun `space before and after operator fix adds spaces around operator with no existing spaces`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.SUM, 1, 2),
                NumberLiteralToken(5, 1, 3),
            )

        val formatter = Formatter(listOf(SpaceBeforeAndAfterOperatorFix()))
        val result = formatter.format(tokens)

        assertEquals("x + 5", result)
    }

    @Test
    fun `space before and after operator fix adds space before operator when missing`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.SUM, 1, 2),
                WhiteSpaceToken(1, 3),
                NumberLiteralToken(5, 1, 4),
            )

        val formatter = Formatter(listOf(SpaceBeforeAndAfterOperatorFix()))
        val result = formatter.format(tokens)

        assertEquals("x + 5", result)
    }

    @Test
    fun `space before and after operator fix adds space after operator when missing`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.SUM, 1, 3),
                NumberLiteralToken(5, 1, 4),
            )

        val formatter = Formatter(listOf(SpaceBeforeAndAfterOperatorFix()))
        val result = formatter.format(tokens)

        assertEquals("x + 5", result)
    }

    @Test
    fun `space before and after operator fix preserves existing spaces around operator`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.SUM, 1, 3),
                WhiteSpaceToken(1, 4),
                NumberLiteralToken(5, 1, 5),
            )

        val formatter = Formatter(listOf(SpaceBeforeAndAfterOperatorFix()))
        val result = formatter.format(tokens)

        assertEquals("x + 5", result)
    }

    @Test
    fun `space before and after operator fix handles different operation types`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.MINUS, 1, 2),
                NumberLiteralToken(3, 1, 3),
            )

        val formatter = Formatter(listOf(SpaceBeforeAndAfterOperatorFix()))
        val result = formatter.format(tokens)

        assertEquals("x - 3", result)
    }

    @Test
    fun `space before and after operator fix ignores operators at start of token list`() {
        val tokens =
            listOf(
                OperationToken(OperationEnum.SUM, 1, 1),
                NumberLiteralToken(5, 1, 2),
            )

        val formatter = Formatter(listOf(SpaceBeforeAndAfterOperatorFix()))
        val result = formatter.format(tokens)

        // Should not modify operators at start (no previous token)
        assertEquals("+5", result)
    }

    @Test
    fun `space before and after operator fix ignores operators at end of token list`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.SUM, 1, 2),
            )

        val formatter = Formatter(listOf(SpaceBeforeAndAfterOperatorFix()))
        val result = formatter.format(tokens)

        // Should not modify operators at end (no next token)
        assertEquals("x+", result)
    }

    @Test
    fun `space before and after operator fix handles single token list`() {
        val tokens = listOf(OperationToken(OperationEnum.SUM, 1, 1))

        val formatter = Formatter(listOf(SpaceBeforeAndAfterOperatorFix()))
        val result = formatter.format(tokens)

        assertEquals("+", result)
    }

    @Test
    fun `space before and after operator fix handles empty token list`() {
        val tokens = emptyList<TokenInterface>()

        val formatter = Formatter(listOf(SpaceBeforeAndAfterOperatorFix()))
        val result = formatter.format(tokens)

        assertEquals("", result)
    }

    @Test
    fun `space before and after operator fix processes only first operator found`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.SUM, 1, 2),
                NumberLiteralToken(5, 1, 3),
                OperationToken(OperationEnum.MINUS, 1, 4),
                NumberLiteralToken(2, 1, 5),
            )

        val formatter = Formatter(listOf(SpaceBeforeAndAfterOperatorFix()))
        val result = formatter.format(tokens)

        // Due to early return, only first operator gets processed
        assertEquals("x + 5-2", result)
    }

    @Test
    fun `space before and after operator fix only applies when configured`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.SUM, 1, 2),
                NumberLiteralToken(5, 1, 3),
            )

        val configMap = mapOf("mandatory-space-surrounding-operations" to JsonPrimitive(false))
        val fix = SpaceBeforeAndAfterOperatorFix()

        assertFalse(fix.applies(configMap))
    }
}
