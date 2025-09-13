package linter

import common.exception.InvalidCamelCaseException
import common.exception.InvalidPascalCaseException
import common.exception.InvalidSnakeCaseException
import common.token.VariableToken
import common.token.abs.TokenInterface
import lexer.Lexer
import linter.rules.required.CamelCaseRule
import linter.rules.required.PascalCaseRule
import linter.rules.required.PrintLnSimpleArgumentRule
import linter.rules.required.SnakeCaseRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class LinterTestOnePointZero {
    private fun tokenizeCode(code: String): List<TokenInterface> {
        val lexer = Lexer()
        return lexer.lex(code)
    }

    @Test
    fun `CamelCaseRule test`() {
        val linter = Linter(listOf(CamelCaseRule()))
        val codeGood = "let myVariable: number = 5;"
        val codeBad = "let my_variable: number = 5;"
        val codeEdge = "let MyVariable: number = 5;" // PascalCase

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

        assertEquals(0, rule.match(tokens).size)
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

        val exceptions = rule.match(tokens)
        assertEquals(3, exceptions.size)
        assertTrue(exceptions.all { it is InvalidCamelCaseException })
    }

    @Test
    fun `SnakeCaseRule test`() {
        val linter = Linter(listOf(SnakeCaseRule()))
        val codeGood = "let my_variable: number = 5;"
        val codeBad = "let myVariable: number = 5;"
        val codeEdge = "let my__variable: number = 5;" // double underscore

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
        assertEquals(3, exception.size)
        assertTrue(exception.all { it is InvalidSnakeCaseException })
    }

    @Test
    fun `PascalCaseRule test`() {
        val linter = Linter(listOf(PascalCaseRule()))
        val codeGood = "let MyVariable: number = 5;"
        val codeBad = "let myVariable: number = 5;"
        // val codeEdge = "let Myvariable: number = 5;" // only first letter uppercase

        assertTrue(linter.lint(tokenizeCode(codeGood)).isEmpty())
        assertFalse(linter.lint(tokenizeCode(codeBad)).isEmpty())
        // assertFalse(linter.lint(tokenizeCode(codeEdge)).isEmpty()) -> fixme this case
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

        assertEquals(0, rule.match(tokens).size)
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
        assertEquals(3, exception.size)
        assertTrue(exception.all { it is InvalidPascalCaseException })
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
        assertTrue(result.any { it is InvalidCamelCaseException })
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
        assertTrue(camelRule.match(camelTokens).isEmpty())
        assertTrue(pascalRule.match(camelTokens).all { it is InvalidPascalCaseException })
        assertTrue(snakeRule.match(camelTokens).all { it is InvalidSnakeCaseException })

        val pascalTokens = listOf(VariableToken("ValidPascalCase", 1, 1))
        assertTrue(camelRule.match(pascalTokens).all { it is InvalidCamelCaseException })
        assertTrue(pascalRule.match(pascalTokens).isEmpty())
        assertTrue(snakeRule.match(pascalTokens).all { it is InvalidSnakeCaseException })

        val snakeTokens = listOf(VariableToken("valid_snake_case", 1, 1))
        assertTrue(camelRule.match(snakeTokens).all { it is InvalidCamelCaseException })
        assertTrue(pascalRule.match(snakeTokens).all { it is InvalidPascalCaseException })
        assertTrue(snakeRule.match(snakeTokens).isEmpty())
    }
}
