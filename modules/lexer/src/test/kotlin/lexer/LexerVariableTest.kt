package lexer

import Linter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import syntax.rules.CamelCaseRule
import syntax.rules.PascalCaseRule
import syntax.rules.SnakeCaseRule

class LexerVariableTest {
    @Test
    fun generateVariableToken() {
        val lexer = Lexer("myVariable another_var OtherVariable var123")
        val tokens = lexer.lex()
        assert(tokens.size == 4) { "Expected 4 tokens, got ${tokens.size}" }
        assert(tokens.all { it is token.VariableToken }) { "All tokens should be VariableToken" }
    }

    @Test
    fun rulesCaseVariableToken() {
        val camelLexer = Lexer("camelCase", linter = Linter(listOf(CamelCaseRule())))
        val pascalLexer = Lexer("PascalCase", linter = Linter(listOf(PascalCaseRule())))
        val snakeLexer = Lexer("snake_case", linter = Linter(listOf(SnakeCaseRule())))
        val camelTokens = camelLexer.lex()
        val pascalTokens = pascalLexer.lex()
        val snakeTokens = snakeLexer.lex()
        assert(camelTokens.size == 1 && camelTokens[0] is token.VariableToken) { "Expected camelCase to be recognized as VariableToken" }
        assert(pascalTokens.size == 1 && pascalTokens[0] is token.VariableToken) { "Expected PascalCase to be recognized as VariableToken" }
        assert(snakeTokens.size == 1 && snakeTokens[0] is token.VariableToken) { "Expected snake_case to be recognized as VariableToken" }
    }

    @Test
    fun invalidVariableToken() {
        val invalidCamelLexer = Lexer("snake_case PascalCase", Linter(listOf(CamelCaseRule())))
        val invalidPascalLexer = Lexer("camelCase snake_case", Linter(listOf(PascalCaseRule())))
        val invalidSnakeLexer = Lexer("PascalCase camelCase", Linter(listOf(SnakeCaseRule())))
        assertThrows<exception.InvalidCamelCaseException> {
            invalidCamelLexer.lex()
        }
        assertThrows<exception.InvalidPascalCaseException> {
            invalidPascalLexer.lex()
        }
        assertThrows<exception.InvalidSnakeCaseException> {
            invalidSnakeLexer.lex()
        }
    }
}
