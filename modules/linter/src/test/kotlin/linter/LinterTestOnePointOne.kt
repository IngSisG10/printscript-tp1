package linter

import common.token.abs.TokenInterface
import lexer.Lexer

class LinterTestOnePointOne {
    private fun tokenizeCode(code: String): List<TokenInterface> {
        val lexer = Lexer()
        return lexer.lex(code)
    }

//    @Test
//    fun `ReadInputSimpleArgumentRule test`() {
//        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
//        val codeGood = "readInput(\"Enter value\");"
//        val codeBad = "readInput(1 + 2);"
//        val codeEdge = "readInput(variable);" // variable, not literal
//
//        assertTrue(linter.lint(tokenizeCode(codeGood)).isEmpty())
//        assertFalse(linter.lint(tokenizeCode(codeBad)).isEmpty())
//        assertFalse(linter.lint(tokenizeCode(codeEdge)).isEmpty())
//    }
//
//    @Test
//    fun `ReadInputSimpleArgumentRule should fail for empty argument`() {
//        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
//        val code = "readInput();"
//        assertFalse(linter.lint(tokenizeCode(code)).isEmpty())
//    }
//
//    @Test
//    fun `ReadInputSimpleArgumentRule should fail for multiple arguments`() {
//        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
//        val code = "readInput(\"Enter value\", 42);"
//        assertFalse(linter.lint(tokenizeCode(code)).isEmpty())
//    }
//
//    @Test
//    fun `ReadInputSimpleArgumentRule should fail for non-string literal argument`() {
//        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
//        val code = "readInput(42);"
//        assertFalse(linter.lint(tokenizeCode(code)).isEmpty())
//    }
//
//    @Test
//    fun `ReadInputSimpleArgumentRule should pass for string literal with whitespace`() {
//        val linter = Linter(listOf(ReadInputSimpleArgumentRule()))
//        val code = "readInput(\"   \");"
//        assertTrue(linter.lint(tokenizeCode(code)).isEmpty())
//    }
}
