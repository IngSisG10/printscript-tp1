package formatter

import common.enums.OperationEnum
import common.token.NumberLiteralToken
import common.token.OperationToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import formatter.fixes.required.NoSpaceAfterEqualFix
import kotlin.test.Test
import kotlin.test.assertEquals

class NoSpaceAfterEqualTest {
    @Test
    fun `no space after equal fix removes space after equals`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                WhiteSpaceToken(1, 3),
                NumberLiteralToken(5, 1, 4),
            )

        val formatter = Formatter(listOf(NoSpaceAfterEqualFix()))
        val result = formatter.format(tokens)

        assertEquals("x=5", result)
    }

    @Test
    fun `no space after equal fix removes space before equals`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                NumberLiteralToken(5, 1, 4),
            )

        val formatter = Formatter(listOf(NoSpaceAfterEqualFix()))
        val result = formatter.format(tokens)

        assertEquals("x=5", result)
    }

    @Test
    fun `no space after equal fix removes spaces before and after equals`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                WhiteSpaceToken(1, 4),
                NumberLiteralToken(5, 1, 5),
            )

        val formatter = Formatter(listOf(NoSpaceAfterEqualFix()))
        val result = formatter.format(tokens)

        assertEquals("x=5", result)
    }

    @Test
    fun `no space after equal fix ignores non-equal operations`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.SUM, 1, 3),
                WhiteSpaceToken(1, 4),
                NumberLiteralToken(5, 1, 5),
            )

        val formatter = Formatter(listOf(NoSpaceAfterEqualFix()))
        val result = formatter.format(tokens)

        // Should not remove spaces around non-equal operations
        assertEquals("x + 5", result)
    }

    @Test
    fun `no space after equal fix handles equals at beginning of token list`() {
        val tokens =
            listOf(
                OperationToken(OperationEnum.EQUAL, 1, 1),
                WhiteSpaceToken(1, 2),
                NumberLiteralToken(5, 1, 3),
            )

        val formatter = Formatter(listOf(NoSpaceAfterEqualFix()))
        val result = formatter.format(tokens)

        assertEquals("=5", result)
    }

    @Test
    fun `no space after equal fix handles equals at end of token list`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
            )

        val formatter = Formatter(listOf(NoSpaceAfterEqualFix()))
        val result = formatter.format(tokens)

        assertEquals("x=", result)
    }

    @Test
    fun `no space after equal fix only applies when configured`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                WhiteSpaceToken(1, 4),
                NumberLiteralToken(5, 1, 5),
            )
    }
}
