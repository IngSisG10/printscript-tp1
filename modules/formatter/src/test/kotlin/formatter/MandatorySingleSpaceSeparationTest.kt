package formatter

import common.enums.TypeEnum
import common.token.EndSentenceToken
import common.token.NewLineToken
import common.token.TypeDeclaratorToken
import common.token.TypeToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.required.MandatorySingleSpaceSeparation
import kotlinx.serialization.json.JsonPrimitive
import org.junit.jupiter.api.Assertions.assertFalse
import kotlin.test.Test
import kotlin.test.assertEquals

class MandatorySingleSpaceSeparationTest {
    @Test
    fun `mandatory single space separation removes multiple spaces and adds single spaces`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                WhiteSpaceToken(1, 3),
                WhiteSpaceToken(1, 4),
                TypeDeclaratorToken(1, 5),
                TypeToken(TypeEnum.STRING, 1, 6),
            )

        val formatter = Formatter(listOf(MandatorySingleSpaceSeparation()))
        val result = formatter.format(tokens)

        // Should remove all existing spaces and add exactly one space between tokens
        assertEquals("x : string", result)
    }

    @Test
    fun `mandatory single space separation handles tokens with no spaces`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                TypeDeclaratorToken(1, 2),
                TypeToken(TypeEnum.STRING, 1, 3),
            )

        val formatter = Formatter(listOf(MandatorySingleSpaceSeparation()))
        val result = formatter.format(tokens)

        // Should add single spaces between all tokens
        assertEquals("x : string", result)
    }

    @Test
    fun `mandatory single space separation preserves newlines and does not add spaces around them`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                TypeDeclaratorToken(1, 3),
                NewLineToken(1, 4),
                VariableToken("y", 2, 1),
                TypeDeclaratorToken(2, 2),
            )

        val formatter = Formatter(listOf(MandatorySingleSpaceSeparation()))
        val result = formatter.format(tokens)

        // Should not add spaces before or after newlines
        assertEquals("x :\ny :", result)
    }

    @Test
    fun `mandatory single space separation does not add space before semicolon`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                WhiteSpaceToken(1, 3),
                TypeDeclaratorToken(1, 4),
                WhiteSpaceToken(1, 5),
                TypeToken(TypeEnum.STRING, 1, 6),
                WhiteSpaceToken(1, 7),
                EndSentenceToken(1, 8),
            )

        val formatter = Formatter(listOf(MandatorySingleSpaceSeparation()))
        val result = formatter.format(tokens)

        // Should not add space immediately before EndSentenceToken
        assertEquals("x : string;", result)
    }

    @Test
    fun `mandatory single space separation adds space after semicolon when followed by other tokens`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                TypeDeclaratorToken(1, 2),
                EndSentenceToken(1, 3),
                VariableToken("y", 1, 4),
            )

        val formatter = Formatter(listOf(MandatorySingleSpaceSeparation()))
        val result = formatter.format(tokens)

        // Should add space after semicolon when followed by another token
        assertEquals("x :; y", result)
    }

    @Test
    fun `mandatory single space separation handles empty token list`() {
        val tokens = emptyList<TokenInterface>()

        val formatter = Formatter(listOf(MandatorySingleSpaceSeparation()))
        val result = formatter.format(tokens)

        assertEquals("", result)
    }

    @Test
    fun `mandatory single space separation handles single token`() {
        val tokens = listOf(VariableToken("x", 1, 1))

        val formatter = Formatter(listOf(MandatorySingleSpaceSeparation()))
        val result = formatter.format(tokens)

        assertEquals("x", result)
    }

    @Test
    fun `mandatory single space separation handles complex mixed spacing`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                WhiteSpaceToken(1, 3),
                TypeDeclaratorToken(1, 4),
                NewLineToken(1, 5),
                WhiteSpaceToken(2, 1),
                VariableToken("y", 2, 2),
                EndSentenceToken(2, 3),
                WhiteSpaceToken(2, 4),
                WhiteSpaceToken(2, 5),
                NewLineToken(2, 6),
            )

        val formatter = Formatter(listOf(MandatorySingleSpaceSeparation()))
        val result = formatter.format(tokens)

        // Should normalize all spacing while preserving newline behavior
        assertEquals("x :\ny;\n", result)
    }

    @Test
    fun `mandatory single space separation handles consecutive newlines`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                NewLineToken(1, 2),
                NewLineToken(2, 1),
                VariableToken("y", 3, 1),
            )

        val formatter = Formatter(listOf(MandatorySingleSpaceSeparation()))
        val result = formatter.format(tokens)

        // Should preserve consecutive newlines without adding spaces
        assertEquals("x\n\ny", result)
    }

    @Test
    fun `mandatory single space separation only applies when configured`() {
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                WhiteSpaceToken(1, 3),
                TypeDeclaratorToken(1, 4),
            )

        val configMap = mapOf("mandatory-single-space-separation" to JsonPrimitive(false))
        val fix = MandatorySingleSpaceSeparation()

        // applies() should return false when configuration is false
        assertFalse(fix.applies(configMap))
    }
}
