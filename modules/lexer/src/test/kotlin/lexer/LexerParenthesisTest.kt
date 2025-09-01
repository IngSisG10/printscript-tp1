package lexer

import Linter
import exception.NoMatchingParenthesisException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import syntax.rules.NoMatchingParenthesisRule
import token.ParenthesisToken
import token.VariableToken

class LexerParenthesisTest {
    @Test
    fun unmatchedOpeningParenthesisThrows() {
        assertThrows(NoMatchingParenthesisException::class.java) {
            Lexer(
                "(()",
                linter =
                    Linter(
                        listOf(
                            NoMatchingParenthesisRule(),
                        ),
                    ),
            ).lex()
        }
    }

    @Test
    fun unmatchedClosingParenthesisThrows() {
        assertThrows(NoMatchingParenthesisException::class.java) {
            Lexer(
                "())",
                linter =
                    Linter(
                        listOf(
                            NoMatchingParenthesisRule(),
                        ),
                    ),
            ).lex()
        }
    }

    @Test
    fun matchedParenthesesNoException() {
        val tokens = Lexer("(something)").lex()
        assert(tokens.isNotEmpty()) { "Expected tokens to be generated for matched parentheses" }
        assert(tokens.any { it is ParenthesisToken }) { "Expected Parenthesis Token" }
        assert((tokens[0] as ParenthesisToken).value.any { it is VariableToken }) { "Expected Variable Token Inside Parenthesis Token" }
    }

    @Test
    fun nestedParenthesesNoException() {
        val tokens = Lexer("(outer (inner))").lex()
        assert(tokens.isNotEmpty()) { "Expected tokens to be generated for nested parentheses" }
        assert(tokens.any { it is ParenthesisToken }) { "Expected Parenthesis Token" }
        val outerParenthesis = tokens.filterIsInstance<ParenthesisToken>().firstOrNull()
        assert(outerParenthesis != null) { "Expected Outer Parenthesis Token" }
        if (outerParenthesis != null) {
            assert(outerParenthesis.value.any { it is VariableToken }) { "Expected Variable Token Inside Outer Parenthesis Token" }
        }
        val innerParenthesis = outerParenthesis?.value?.filterIsInstance<ParenthesisToken>()?.firstOrNull()
        assert(innerParenthesis != null) { "Expected Inner Parenthesis Token" }
        if (innerParenthesis != null) {
            assert(innerParenthesis.value.any { it is VariableToken }) { "Expected Variable Token Inside Inner Parenthesis Token" }
        }
    }
}
