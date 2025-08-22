package lexer

import lexer.syntax.rules.CamelCaseRule
import lexer.syntax.rules.PascalCaseRule
import lexer.syntax.rules.SnakeCaseRule
import lexer.token.rules.IdentifierRule
import lexer.token.rules.KeywordRule
import lexer.token.rules.NumberLiteralRule
import lexer.token.rules.ParenthesisRule
import lexer.token.rules.SingleCharRule
import lexer.token.rules.StringLiteralRule
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LexerVariableTest {
    private val camelCaseRules =
        listOf(
            StringLiteralRule(),
            NumberLiteralRule(),
            KeywordRule(),
            ParenthesisRule(),
            SingleCharRule(),
            IdentifierRule(
                rules = listOf(CamelCaseRule()),
            ),
        )

    private val snakeCaseRules =
        listOf(
            StringLiteralRule(),
            NumberLiteralRule(),
            KeywordRule(),
            ParenthesisRule(),
            SingleCharRule(),
            IdentifierRule(
                rules = listOf(SnakeCaseRule()),
            ),
        )

    private val pascalCaseRules =
        listOf(
            StringLiteralRule(),
            NumberLiteralRule(),
            KeywordRule(),
            ParenthesisRule(),
            SingleCharRule(),
            IdentifierRule(
                rules = listOf(PascalCaseRule()),
            ),
        )

    @Test
    fun generateVariableToken() {
        val lexer = Lexer("myVariable another_var OtherVariable var123")
        val tokens = lexer.lex()
        assert(tokens.size == 4) { "Expected 4 tokens, got ${tokens.size}" }
        assert(tokens.all { it is token.VariableToken }) { "All tokens should be VariableToken" }
    }

    @Test
    fun rulesCaseVariableToken() {
        val camelLexer = Lexer("camelCase", tokenRules = camelCaseRules)
        val pascalLexer = Lexer("PascalCase", tokenRules = pascalCaseRules)
        val snakeLexer = Lexer("snake_case", tokenRules = snakeCaseRules)
        val camelTokens = camelLexer.lex()
        val pascalTokens = pascalLexer.lex()
        val snakeTokens = snakeLexer.lex()
        assert(camelTokens.size == 1 && camelTokens[0] is token.VariableToken) { "Expected camelCase to be recognized as VariableToken" }
        assert(pascalTokens.size == 1 && pascalTokens[0] is token.VariableToken) { "Expected PascalCase to be recognized as VariableToken" }
        assert(snakeTokens.size == 1 && snakeTokens[0] is token.VariableToken) { "Expected snake_case to be recognized as VariableToken" }
    }

    @Test
    fun invalidVariableToken() {
        val invalidCamelLexer = Lexer("snake_case PascalCase", tokenRules = camelCaseRules)
        val invalidPascalLexer = Lexer("camelCase snake_case", tokenRules = pascalCaseRules)
        val invalidSnakeLexer = Lexer("PascalCase camelCase", tokenRules = snakeCaseRules)
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
