package formatter

import common.enums.OperationEnum
import common.token.NumberLiteralToken
import common.token.OperationToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.required.SpaceBeforeAndAfterEqualFix
import kotlinx.serialization.json.JsonPrimitive
import org.junit.jupiter.api.Assertions.assertFalse
import kotlin.test.Test
import kotlin.test.assertEquals

class SpaceBeforeAndAfterEqualFixTest {
    @Test
    fun `space before and after equal fix adds spaces around equal with no existing spaces`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                NumberLiteralToken(5, 1, 3),
            )

        val formatter = Formatter(listOf(SpaceBeforeAndAfterEqualFix()))
        val result = formatter.format(tokens)

        assertEquals("x = 5", result)
    }

    @Test
    fun `space before and after equal fix adds space before equal when missing`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                WhiteSpaceToken(1, 3),
                NumberLiteralToken(5, 1, 4),
            )

        val formatter = Formatter(listOf(SpaceBeforeAndAfterEqualFix()))
        val result = formatter.format(tokens)

        assertEquals("x = 5", result)
    }

    @Test
    fun `space before and after equal fix adds space after equal when missing`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                NumberLiteralToken(5, 1, 4),
            )

        val formatter = Formatter(listOf(SpaceBeforeAndAfterEqualFix()))
        val result = formatter.format(tokens)

        assertEquals("x = 5", result)
    }

    @Test
    fun `space before and after equal fix preserves existing spaces around equal`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                WhiteSpaceToken(1, 4),
                NumberLiteralToken(5, 1, 5),
            )

        val formatter = Formatter(listOf(SpaceBeforeAndAfterEqualFix()))
        val result = formatter.format(tokens)

        assertEquals("x = 5", result)
    }

    @Test
    fun `space before and after equal fix ignores non-equal operations`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.SUM, 1, 2),
                NumberLiteralToken(5, 1, 3),
            )

        val formatter = Formatter(listOf(SpaceBeforeAndAfterEqualFix()))
        val result = formatter.format(tokens)

        // Should not modify non-equal operations
        assertEquals("x+5", result)
    }

    @Test
    fun `space before and after equal fix ignores equal at start of token list`() {
        val tokens =
            listOf(
                OperationToken(OperationEnum.EQUAL, 1, 1),
                NumberLiteralToken(5, 1, 2),
            )

        val formatter = Formatter(listOf(SpaceBeforeAndAfterEqualFix()))
        val result = formatter.format(tokens)

        // Loop starts at index 1, so equal at index 0 is ignored
        assertEquals("=5", result)
    }

    @Test
    fun `space before and after equal fix handles complex expression with mixed operations`() {
        val tokens =
            listOf(
                VariableToken("result", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                VariableToken("x", 1, 3),
                OperationToken(OperationEnum.SUM, 1, 4),
                NumberLiteralToken(5, 1, 5),
            )

        val formatter = Formatter(listOf(SpaceBeforeAndAfterEqualFix()))
        val result = formatter.format(tokens)

        assertEquals("result = x+5", result)
    }

    @Test
    fun `space before and after equal fix handles single token list`() {
        val tokens = listOf(OperationToken(OperationEnum.EQUAL, 1, 1))

        val formatter = Formatter(listOf(SpaceBeforeAndAfterEqualFix()))
        val result = formatter.format(tokens)

        assertEquals("=", result)
    }

    @Test
    fun `space before and after equal fix handles empty token list`() {
        val tokens = emptyList<TokenInterface>()

        val formatter = Formatter(listOf(SpaceBeforeAndAfterEqualFix()))
        val result = formatter.format(tokens)

        assertEquals("", result)
    }

    @Test
    fun `space before and after equal fix only applies when configured`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                NumberLiteralToken(5, 1, 3),
            )

        val configMap = mapOf("space_before_and_after_equal" to JsonPrimitive(false))
        val fix = SpaceBeforeAndAfterEqualFix()

        assertFalse(fix.applies(configMap))
    }
}
