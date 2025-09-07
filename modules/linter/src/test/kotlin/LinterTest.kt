import enums.OperationEnum
import enums.TypeEnum
import exception.InvalidCamelCaseException
import exception.InvalidPascalCaseException
import exception.InvalidSnakeCaseException
import exception.NoSpaceAfterAssignationException
import exception.NoSpaceAfterColonException
import exception.NoSpaceBeforeAssignationException
import exception.NoSpaceBeforeColonException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import syntax.rules.CamelCaseRule
import syntax.rules.LineJumpAfterSemicolonRule
import syntax.rules.PascalCaseRule
import syntax.rules.SnakeCaseRule
import syntax.rules.SpaceAfterAssignationRule
import syntax.rules.SpaceAfterColonRule
import syntax.rules.SpaceBeforeAssignationRule
import syntax.rules.SpaceBeforeColonRule
import token.EndSentenceToken
import token.NumberLiteralToken
import token.OperationToken
import token.StringLiteralToken
import token.TypeDeclaratorToken
import token.TypeToken
import token.VariableToken
import token.WhiteSpaceToken
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
    fun `SpaceAfterColonRule should pass when space exists after colon`() {
        val rule = SpaceAfterColonRule()
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                TypeDeclaratorToken(1, 2),
                WhiteSpaceToken(1, 3),
                TypeToken(TypeEnum.STRING, 1, 4),
            )

        assertNull(rule.match(tokens))
        assertTrue(rule.matchWithData(tokens).isEmpty())
    }

    @Test
    fun `SpaceAfterColonRule should fail when no space after colon`() {
        val rule = SpaceAfterColonRule()
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                TypeDeclaratorToken(1, 2),
                TypeToken(TypeEnum.STRING, 1, 3), // No space after colon
            )

        assertThrows<NoSpaceAfterColonException> {
            rule.match(tokens)
        }

        val linterData = rule.matchWithData(tokens)
        assertEquals(1, linterData.size)
        assertTrue(linterData[0].exception is NoSpaceAfterColonException)
        assertEquals(1, linterData[0].position)
    }

    @Test
    fun `SpaceBeforeColonRule should pass when space exists before colon`() {
        val rule = SpaceBeforeColonRule()
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                WhiteSpaceToken(1, 2),
                TypeDeclaratorToken(1, 3),
                TypeToken(TypeEnum.STRING, 1, 4),
            )

        assertNull(rule.match(tokens))
        assertTrue(rule.matchWithData(tokens).isEmpty())
    }

    @Test
    fun `SpaceBeforeColonRule should fail when no space before colon`() {
        val rule = SpaceBeforeColonRule()
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                TypeDeclaratorToken(1, 2), // No space before colon
                TypeToken(TypeEnum.STRING, 1, 3),
            )

        assertThrows<NoSpaceBeforeColonException> {
            rule.match(tokens)
        }

        val linterData = rule.matchWithData(tokens)
        assertEquals(1, linterData.size)
        assertTrue(linterData[0].exception is NoSpaceBeforeColonException)
        assertEquals(1, linterData[0].position)
    }

    @Test
    fun `SpaceAfterAssignationRule should pass when space exists after equals`() {
        val rule = SpaceAfterAssignationRule()
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                WhiteSpaceToken(1, 4),
                NumberLiteralToken(5, 1, 5),
            )

        assertNull(rule.match(tokens))
        assertTrue(rule.matchWithData(tokens).isEmpty())
    }

    @Test
    fun `SpaceAfterAssignationRule should fail when no space after equals`() {
        val rule = SpaceAfterAssignationRule()
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                NumberLiteralToken(5, 1, 4), // No space after equals
            )

        assertThrows<NoSpaceAfterAssignationException> {
            rule.match(tokens)
        }

        val linterData = rule.matchWithData(tokens)
        assertEquals(1, linterData.size)
        assertTrue(linterData[0].exception is NoSpaceAfterAssignationException)
        assertEquals(2, linterData[0].position)
    }

    @Test
    fun `SpaceBeforeAssignationRule should pass when space exists before equals`() {
        val rule = SpaceBeforeAssignationRule()
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
                WhiteSpaceToken(1, 4),
                NumberLiteralToken(5, 1, 5),
            )

        assertNull(rule.match(tokens))
        assertTrue(rule.matchWithData(tokens).isEmpty())
    }

    @Test
    fun `SpaceBeforeAssignationRule should fail when no space before equals`() {
        val rule = SpaceBeforeAssignationRule()
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                OperationToken(OperationEnum.EQUAL, 1, 2), // No space before equals
                WhiteSpaceToken(1, 3),
                NumberLiteralToken(5, 1, 4),
            )

        assertThrows<NoSpaceBeforeAssignationException> {
            rule.match(tokens)
        }

        val linterData = rule.matchWithData(tokens)
        assertEquals(1, linterData.size)
        assertTrue(linterData[0].exception is NoSpaceBeforeAssignationException)
        assertEquals(1, linterData[0].position)
    }

    @Test
    fun `NewLineAfterSemicolonRule should not throw when properly implemented`() {
        val rule = LineJumpAfterSemicolonRule()
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                EndSentenceToken(1, 2),
            )

        // Since the rule is commented out, it should return null
        assertNull(rule.match(tokens))
        assertTrue(rule.matchWithData(tokens).isEmpty())
    }

    @Test
    fun `multiple naming rules should work independently`() {
        val camelRule = CamelCaseRule()
        val pascalRule = PascalCaseRule()
        val snakeRule = SnakeCaseRule()

        val camelTokens = listOf(VariableToken("validCamelCase", 1, 1))
        assertNull(camelRule.match(camelTokens))
        assertTrue(pascalRule.match(camelTokens) is InvalidPascalCaseException)
        assertTrue(snakeRule.match(camelTokens) is InvalidSnakeCaseException)

        val pascalTokens = listOf(VariableToken("ValidPascalCase", 1, 1))
        assertTrue(camelRule.match(pascalTokens) is InvalidCamelCaseException)
        assertNull(pascalRule.match(pascalTokens))
        assertTrue(snakeRule.match(pascalTokens) is InvalidSnakeCaseException)

        val snakeTokens = listOf(VariableToken("valid_snake_case", 1, 1))
        assertTrue(camelRule.match(snakeTokens) is InvalidCamelCaseException)
        assertTrue(pascalRule.match(snakeTokens) is InvalidPascalCaseException)
        assertNull(snakeRule.match(snakeTokens))
    }

    @Test
    fun `combination of spacing rules should work together`() {
        val spaceAfterColonRule = SpaceAfterColonRule()
        val spaceBeforeColonRule = SpaceBeforeColonRule()
        val spaceAfterAssignRule = SpaceAfterAssignationRule()
        val spaceBeforeAssignRule = SpaceBeforeAssignationRule()

        val validTokens =
            listOf(
                VariableToken("variable", 1, 1),
                WhiteSpaceToken(1, 2),
                TypeDeclaratorToken(1, 3),
                WhiteSpaceToken(1, 4),
                TypeToken(TypeEnum.STRING, 1, 5),
                WhiteSpaceToken(1, 6),
                OperationToken(OperationEnum.EQUAL, 1, 7),
                WhiteSpaceToken(1, 8),
                StringLiteralToken("value", 1, 9),
            )

        assertNull(spaceAfterColonRule.match(validTokens))
        assertNull(spaceBeforeColonRule.match(validTokens))
        assertNull(spaceAfterAssignRule.match(validTokens))
        assertNull(spaceBeforeAssignRule.match(validTokens))
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

        val emptyTokens = emptyList<token.abs.TokenInterface>()

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
