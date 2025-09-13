package linter

import common.enums.OperationEnum
import common.enums.TypeEnum
import common.exception.NoSpaceAfterAssignationException
import common.exception.NoSpaceAfterColonException
import common.exception.NoSpaceBeforeColonException
import common.token.NumberLiteralToken
import common.token.OperationToken
import common.token.TypeDeclaratorToken
import common.token.TypeToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import exception.NoSpaceBeforeAssignationException
import linter.rules.custom.SpaceAfterAssignationRule
import linter.rules.custom.SpaceAfterColonRule
import linter.rules.custom.SpaceBeforeAssignationRule
import linter.rules.custom.SpaceBeforeColonRule
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LinterCustomTests {
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

//    @Test
//    fun `NewLineAfterSemicolonRule should not throw when properly implemented`() {
//        val rule = LineJumpAfterSemicolonRule()
//        val tokens =
//            listOf(
//                VariableToken("variable", 1, 1),
//                EndSentenceToken(1, 2),
//            )
//
//        // Since the rule is commented out, it should return null
//
//        assertThrows<NoNewLineAfterSemiColon> {
//            rule.match(tokens)
//        }
//        assertTrue(rule.matchWithData(tokens).isEmpty())
//    }
}
