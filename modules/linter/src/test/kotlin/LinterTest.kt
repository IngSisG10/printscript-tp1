import common.enums.OperationEnum
import common.exception.InvalidCamelCaseException
import common.exception.InvalidPascalCaseException
import common.exception.InvalidSnakeCaseException
import common.exception.NoSpaceAfterAssignationException
import common.token.NumberLiteralToken
import common.token.OperationToken
import common.token.TypeDeclaratorToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import linter.Linter
import linter.rules.custom.LineJumpAfterSemicolonRule
import linter.rules.custom.SpaceAfterAssignationRule
import linter.rules.custom.SpaceAfterColonRule
import linter.rules.custom.SpaceBeforeAssignationRule
import linter.rules.custom.SpaceBeforeColonRule
import linter.rules.required.CamelCaseRule
import linter.rules.required.PascalCaseRule
import linter.rules.required.SnakeCaseRule
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LinterTest {
    @Test
    fun `CamelCaseRule should pass for valid camelCase variables`() {
        val rule = CamelCaseRule()
        val tokens =
            listOf(
                VariableToken("validVariable", 1, 1),
                VariableToken("anotherValidVar", 1, 2),
                VariableToken("var123", 1, 3),
            )

        assertNull(rule.match(tokens))
        assertTrue(rule.matchWithData(tokens).isEmpty())
    }

    @Test
    fun `CamelCaseRule should fail for invalid camelCase variables`() {
        val rule = CamelCaseRule()
        val tokens =
            listOf(
                VariableToken("InvalidVariable", 1, 1), // PascalCase
                VariableToken("invalid_variable", 1, 2), // snake_case
                VariableToken("123invalid", 1, 3), // starts with number
            )

        val exception = rule.match(tokens)
        assertTrue(exception is InvalidCamelCaseException)

        val linterData = rule.matchWithData(tokens)
        assertEquals(3, linterData.size)
        assertTrue(linterData.all { it.exception is InvalidCamelCaseException })
    }

    @Test
    fun `PascalCaseRule should pass for valid PascalCase variables`() {
        val rule = PascalCaseRule()
        val tokens =
            listOf(
                VariableToken("ValidVariable", 1, 1),
                VariableToken("AnotherValidVar", 1, 2),
                VariableToken("Var123", 1, 3),
            )

        assertNull(rule.match(tokens))
        assertTrue(rule.matchWithData(tokens).isEmpty())
    }

    @Test
    fun `PascalCaseRule should fail for invalid PascalCase variables`() {
        val rule = PascalCaseRule()
        val tokens =
            listOf(
                VariableToken("invalidVariable", 1, 1), // camelCase
                VariableToken("invalid_variable", 1, 2), // snake_case
                VariableToken("123Invalid", 1, 3), // starts with number
            )

        val exception = rule.match(tokens)
        assertTrue(exception is InvalidPascalCaseException)

        val linterData = rule.matchWithData(tokens)
        assertEquals(3, linterData.size)
        assertTrue(linterData.all { it.exception is InvalidPascalCaseException })
    }

    @Test
    fun `SnakeCaseRule should pass for valid snake_case variables`() {
        val rule = SnakeCaseRule()
        val tokens =
            listOf(
                VariableToken("valid_variable", 1, 1),
                VariableToken("another_valid_var", 1, 2),
                VariableToken("var123", 1, 3),
            )

        assertNull(rule.match(tokens))
        assertTrue(rule.matchWithData(tokens).isEmpty())
    }

    @Test
    fun `SnakeCaseRule should fail for invalid snake_case variables`() {
        val rule = SnakeCaseRule()
        val tokens =
            listOf(
                VariableToken("InvalidVariable", 1, 1), // PascalCase
                VariableToken("invalidVariable", 1, 2), // camelCase
                VariableToken("Invalid_Variable", 1, 3), // mixed case
            )

        val exception = rule.match(tokens)
        assertTrue(exception is InvalidSnakeCaseException)

        val linterData = rule.matchWithData(tokens)
        assertEquals(3, linterData.size)
        assertTrue(linterData.all { it.exception is InvalidSnakeCaseException })
    }

    @Test
    fun `LinterData should contain correct position and exception information`() {
        val rule = CamelCaseRule()
        val tokens =
            listOf(
                VariableToken("validVariable", 1, 1),
                VariableToken("InvalidVariable", 2, 5),
                VariableToken("another_invalid", 3, 10),
            )

        val linterData = rule.matchWithData(tokens)
        assertEquals(2, linterData.size)

        assertEquals(1, linterData[0].position)
        assertTrue(linterData[0].exception is InvalidCamelCaseException)

        assertEquals(2, linterData[1].position)
        assertTrue(linterData[1].exception is InvalidCamelCaseException)
    }

    @Test
    fun `edge case - empty token list should not cause errors`() {
        val rules =
            listOf(
                CamelCaseRule(),
                PascalCaseRule(),
                SnakeCaseRule(),
                SpaceAfterColonRule(),
                SpaceBeforeColonRule(),
                SpaceAfterAssignationRule(),
                SpaceBeforeAssignationRule(),
                LineJumpAfterSemicolonRule(),
            )

        val emptyTokens = emptyList<TokenInterface>()

        rules.forEach { rule ->
            assertNull(rule.match(emptyTokens))
            assertTrue(rule.matchWithData(emptyTokens).isEmpty())
        }
    }

    @Test
    fun `edge case - single token causes index out of bounds in current implementation`() {
        val spaceAfterColonRule = SpaceAfterColonRule()
        val singleColonToken = listOf(TypeDeclaratorToken(1, 1))

        // Current implementation has a bug - it throws IndexOutOfBoundsException
        // when there's no next token after a colon
        assertThrows<IndexOutOfBoundsException> {
            spaceAfterColonRule.match(singleColonToken)
        }

        // The matchWithData method has the same issue
        assertThrows<IndexOutOfBoundsException> {
            spaceAfterColonRule.matchWithData(singleColonToken)
        }
    }

    @Test
    fun `Linter should pass when no rules are violated`() {
        val rules = listOf(CamelCaseRule(), SpaceAfterAssignationRule())
        val linter = Linter(rules)
        val validTokens =
            listOf(
                VariableToken("validVariable", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                WhiteSpaceToken(1, 4),
                NumberLiteralToken(5, 1, 5),
            )

        // Should not throw any exception
        linter.lint(validTokens)
    }

    @Test
    fun `Linter should throw first exception when rules are violated`() {
        val rules = listOf(CamelCaseRule(), SpaceAfterAssignationRule())
        val linter = Linter(rules)
        val invalidTokens =
            listOf(
                VariableToken("InvalidVariable", 1, 1), // Violates CamelCase
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                NumberLiteralToken(5, 1, 4), // Violates SpaceAfterAssignation
            )

        // Should throw the first exception it encounters (CamelCase violation)
        assertThrows<InvalidCamelCaseException> {
            linter.lint(invalidTokens)
        }
    }

    @Test
    fun `Linter formatterLint should collect all violations`() {
        val rules = listOf(CamelCaseRule(), SpaceAfterAssignationRule())
        val linter = Linter(rules)
        val invalidTokens =
            listOf(
                VariableToken("InvalidVariable", 1, 1), // Violates CamelCase
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                NumberLiteralToken(5, 1, 4), // Violates SpaceAfterAssignation
            )

        val linterData = linter.formatterLint(invalidTokens)

        // Should collect both violations
        assertEquals(2, linterData.size)
        assertTrue(linterData.any { it.exception is InvalidCamelCaseException })
        assertTrue(linterData.any { it.exception is NoSpaceAfterAssignationException })
    }

    @Test
    fun `Linter with empty rules list should pass all tokens`() {
        val linter = Linter(emptyList())
        val tokens =
            listOf(
                VariableToken("InvalidVariable", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2),
                NumberLiteralToken(5, 1, 3),
            )

        // Should not throw any exception
        linter.lint(tokens)

        // Should return empty list
        assertTrue(linter.formatterLint(tokens).isEmpty())
    }
}
