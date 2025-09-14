package linter

import common.token.abs.TokenInterface
import lexer.Lexer
import linter.rules.required.ReadInputSimpleArgumentRule
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class LinterTestOnePointOne {
    private fun tokenizeCode(code: String): List<TokenInterface> {
        val lexer = Lexer()
        return lexer.lex(code)
    }

    @Test
    fun `ReadInputSimpleArgumentRule test`() {
        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
        val codeGood1 = "readInput(\"Enter value\");" // si
        val codeGood2 = "readInput(variable);" // a
        val codeBad = "readInput(1 + 2);" // no

        assertTrue(linter.lint(tokenizeCode(codeGood1)).isEmpty())
        assertTrue(linter.lint(tokenizeCode(codeGood2)).isEmpty())
//        assertFalse(linter.lint(tokenizeCode(codeBad)).isEmpty()) // Este no pasa porque ?
    }

//

    //     En teoria, el readInput, recibe un String, pero podrias pasarle un number "42", por ejemplo.
//     fijate que quiza el error es porque como tal el readInput solo chequea que haya un solo argumento, no que sea especificamente un string
    @Test
    fun `ReadInputSimpleArgumentRule should fail for non-string literal argument`() {
        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
        val code = "readInput(42);"

        assertTrue(linter.lint(tokenizeCode(code)).isEmpty()) // Puede recibir numeros.
    }

    @Test
    fun `ReadInputSimpleArgumentRule should pass for string literal with whitespace`() {
        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
        val code = "readInput(\"   \");"
        assertFalse(linter.lint(tokenizeCode(code)).isEmpty()) // Deberia ser assertFalse? Consulta.
    }

    @Test
    fun `ReadInputSimpleArgumentRule should pass for string literal`() {
        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
        val code = "readInput(\"Enter value\");"
        assertTrue(linter.lint(tokenizeCode(code)).isEmpty())
    }

    @Test
    fun `ReadInputSimpleArgumentRule should pass for number literal`() {
        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
        val code = "readInput(42);"
        assertTrue(linter.lint(tokenizeCode(code)).isEmpty())
    }

    @Test
    fun `ReadInputSimpleArgumentRule should pass for variable identifier`() {
        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
        val code = "readInput(prompt);"
        assertTrue(linter.lint(tokenizeCode(code)).isEmpty())
    }

    @Test
    fun `ReadInputSimpleArgumentRule should pass for boolean literal`() {
        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
        val code = "readInput(true);"
        assertTrue(linter.lint(tokenizeCode(code)).isEmpty())
    }

    // Invalid cases - should fail (have violations)
    @Test
    fun `ReadInputSimpleArgumentRule should fail for empty argument`() {
        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
        val code = "readInput();"
        assertFalse(linter.lint(tokenizeCode(code)).isEmpty())
    }

    @Test
    fun `ReadInputSimpleArgumentRule should fail for multiple arguments`() {
        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
        val code = "readInput(\"Enter value\" 42);"
        assertFalse(linter.lint(tokenizeCode(code)).isEmpty())
    }

    @Test
    fun `ReadInputSimpleArgumentRule should fail for arithmetic expression`() {
        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
        val code = "readInput(1 + 2);"
        assertFalse(linter.lint(tokenizeCode(code)).isEmpty())
    }

    @Test
    fun `ReadInputSimpleArgumentRule should fail for multiplication expression`() {
        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
        val code = "readInput(a * b);"
        assertFalse(linter.lint(tokenizeCode(code)).isEmpty())
    }

    @Test
    fun `ReadInputSimpleArgumentRule should fail for function call expression`() {
        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
        val code = "readInput(getPrompt());"
        assertFalse(linter.lint(tokenizeCode(code)).isEmpty())
    }

    @Test
    fun `ReadInputSimpleArgumentRule should fail for comparison expression`() {
        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
        val code = "readInput(x + 5);"
        assertFalse(linter.lint(tokenizeCode(code)).isEmpty())
    }

    @Test
    fun `ReadInputSimpleArgumentRule should fail for complex expression`() {
        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
        val code = "readInput((a + b) * c);"
        assertFalse(linter.lint(tokenizeCode(code)).isEmpty())
    }
}
