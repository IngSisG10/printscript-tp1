package linter

import common.enums.OperationEnum
import common.enums.TypeEnum
import common.exception.InvalidCamelCaseException
import common.exception.InvalidPascalCaseException
import common.exception.InvalidSnakeCaseException
import common.token.OperationToken
import common.token.StringLiteralToken
import common.token.TypeDeclaratorToken
import common.token.TypeToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import lexer.Lexer
import linter.rules.custom.SpaceAfterAssignationRule
import linter.rules.custom.SpaceAfterColonRule
import linter.rules.custom.SpaceBeforeAssignationRule
import linter.rules.custom.SpaceBeforeColonRule
import linter.rules.required.CamelCaseRule
import linter.rules.required.PascalCaseRule
import linter.rules.required.PrintLnSimpleArgumentRule
import linter.rules.required.SnakeCaseRule
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LinterTestOnePointZero {
    private fun tokenizeCode(code: String): List<TokenInterface> {
        val lexer = Lexer()
        return lexer.lex(code)
    }

    @Test
    fun `CamelCaseRule test`() {
        val linter = Linter(listOf(CamelCaseRule()))
        val codeGood = "let myVariable: Number = 5;"
        val codeBad = "let my_variable: Number = 5;"
        val codeEdge = "let MyVariable: Number = 5;" // PascalCase

        assertTrue(linter.lint(tokenizeCode(codeGood)).isEmpty())
        assertFalse(linter.lint(tokenizeCode(codeBad)).isEmpty())
        assertFalse(linter.lint(tokenizeCode(codeEdge)).isEmpty())
    }

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
        // todo: List<Throwable>
        assertTrue(exception is InvalidCamelCaseException)
    }

    @Test
    fun `SnakeCaseRule test`() {
        val linter = Linter(listOf(SnakeCaseRule()))
        val codeGood = "let my_variable: Number = 5;"
        val codeBad = "let myVariable: Number = 5;"
        val codeEdge = "let my__variable: Number = 5;" // double underscore

        assertTrue(linter.lint(tokenizeCode(codeGood)).isEmpty())
        assertFalse(linter.lint(tokenizeCode(codeBad)).isEmpty())
        assertFalse(linter.lint(tokenizeCode(codeEdge)).isEmpty())
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
        kotlin.test.assertTrue(exception is InvalidSnakeCaseException)

        val linterData = rule.matchWithData(tokens)
        assertEquals(3, linterData.size)
        kotlin.test.assertTrue(linterData.all { it.exception is InvalidSnakeCaseException })
    }

    @Test
    fun `PascalCaseRule test`() {
        val linter = Linter(listOf(PascalCaseRule()))
        val codeGood = "let MyVariable: Number = 5;"
        val codeBad = "let myVariable: Number = 5;"
        val codeEdge = "let Myvariable: Number = 5;" // only first letter uppercase

        assertTrue(linter.lint(tokenizeCode(codeGood)).isEmpty())
        assertFalse(linter.lint(tokenizeCode(codeBad)).isEmpty())
        assertFalse(linter.lint(tokenizeCode(codeEdge)).isEmpty())
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
        kotlin.test.assertTrue(rule.matchWithData(tokens).isEmpty())
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
        kotlin.test.assertTrue(exception is InvalidPascalCaseException)

        val linterData = rule.matchWithData(tokens)
        assertEquals(3, linterData.size)
        kotlin.test.assertTrue(linterData.all { it.exception is InvalidPascalCaseException })
    }

    @Test
    fun `PrintLnSimpleArgumentRule test`() {
        val linter = Linter(listOf(PrintLnSimpleArgumentRule()))
        val codeGood = "println(\"Hello world\");"
        val codeBad = "println(1 + 2);"
        val codeEdge = "println(variable);" // variable, not literal

        assertTrue(linter.lint(tokenizeCode(codeGood)).isEmpty())
        assertFalse(linter.lint(tokenizeCode(codeBad)).isEmpty())
        assertFalse(linter.lint(tokenizeCode(codeEdge)).isEmpty())
    }

    @Test
    fun `invalid camel case triggers exception`() {
        val rule = CamelCaseRule()
        val tokens =
            listOf(
                VariableToken("my_variable", 1, 1), // Not camelCase
                VariableToken("validCamel", 2, 2), // Valid camelCase
            )
        val result = rule.match(tokens)
        assertTrue(result.any { it is common.exception.InvalidCamelCaseException })
    }

    @Test
    fun `valid camel case does not trigger exception`() {
        val rule = CamelCaseRule()
        val tokens =
            listOf(
                VariableToken("validCamel", 1, 1),
            )
        val result = rule.match(tokens)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `multiple naming rules should work independently`() {
        val camelRule = CamelCaseRule()
        val pascalRule = PascalCaseRule()
        val snakeRule = SnakeCaseRule()

        val camelTokens = listOf(VariableToken("validCamelCase", 1, 1))
        assertNull(camelRule.match(camelTokens))
        kotlin.test.assertTrue(pascalRule.match(camelTokens) is InvalidPascalCaseException)
        kotlin.test.assertTrue(snakeRule.match(camelTokens) is InvalidSnakeCaseException)

        val pascalTokens = listOf(VariableToken("ValidPascalCase", 1, 1))
        kotlin.test.assertTrue(camelRule.match(pascalTokens) is InvalidCamelCaseException)
        assertNull(pascalRule.match(pascalTokens))
        kotlin.test.assertTrue(snakeRule.match(pascalTokens) is InvalidSnakeCaseException)

        val snakeTokens = listOf(VariableToken("valid_snake_case", 1, 1))
        kotlin.test.assertTrue(camelRule.match(snakeTokens) is InvalidCamelCaseException)
        kotlin.test.assertTrue(pascalRule.match(snakeTokens) is InvalidPascalCaseException)
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
}
