package linter

import common.enums.OperationEnum
import common.enums.TypeEnum
import common.exception.NoNewLineAfterSemiColon
import common.exception.NoSpaceAfterAssignationException
import common.exception.NoSpaceAfterColonException
import common.exception.NoSpaceBeforeColonException
import common.token.EndSentenceToken
import common.token.NumberLiteralToken
import common.token.OperationToken
import common.token.StringLiteralToken
import common.token.TypeDeclaratorToken
import common.token.TypeToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import exception.NoSpaceBeforeAssignationException
import linter.rules.custom.LineJumpAfterSemicolonRule
import linter.rules.custom.SpaceAfterAssignationRule
import linter.rules.custom.SpaceAfterColonRule
import linter.rules.custom.SpaceBeforeAssignationRule
import linter.rules.custom.SpaceBeforeColonRule
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LinterCustomTests {
//    @Test
//    fun `SpaceAfterColonRule should pass when space exists after colon`() {
//        val rule = SpaceAfterColonRule()
//        val tokens =
//            listOf(
//                VariableToken("variable", 1, 1),
//                TypeDeclaratorToken(1, 2),
//                WhiteSpaceToken(1, 3),
//                TypeToken(TypeEnum.STRING, 1, 4),
//            )
//
//        assertNull(rule.match(tokens))
//    }
//
//    @Test
//    fun `SpaceAfterColonRule should fail when no space after colon`() {
//        val rule = SpaceAfterColonRule()
//        val tokens =
//            listOf(
//                VariableToken("variable", 1, 1),
//                TypeDeclaratorToken(1, 2),
//                TypeToken(TypeEnum.STRING, 1, 3), // No space after colon
//            )
//
//        assertThrows<NoSpaceAfterColonException> {
//            rule.match(tokens)
//        }
//    }
//
//    @Test
//    fun `SpaceBeforeColonRule should pass when space exists before colon`() {
//        val rule = SpaceBeforeColonRule()
//        val tokens =
//            listOf(
//                VariableToken("variable", 1, 1),
//                WhiteSpaceToken(1, 2),
//                TypeDeclaratorToken(1, 3),
//                TypeToken(TypeEnum.STRING, 1, 4),
//            )
//
//        val result = rule.match(tokens)
//        assertTrue(result.isEmpty())
//    }
//
//    @Test
//    fun `SpaceBeforeColonRule should fail when no space before colon`() {
//        val rule = SpaceBeforeColonRule()
//        val tokens =
//            listOf(
//                VariableToken("variable", 1, 1),
//                TypeDeclaratorToken(1, 2), // No space before colon
//                TypeToken(TypeEnum.STRING, 1, 3),
//            )
//
//        val result = rule.match(tokens)
//        assertTrue(result.isNotEmpty())
//        assertTrue(result.any { it is NoSpaceBeforeColonException })
//    }
//
//    @Test
//    fun `SpaceAfterAssignationRule should pass when space exists after equals`() {
//        val rule = SpaceAfterAssignationRule()
//        val tokens =
//            listOf(
//                VariableToken("variable", 1, 1),
//                WhiteSpaceToken(1, 2),
//                OperationToken(OperationEnum.EQUAL, 1, 3),
//                WhiteSpaceToken(1, 4),
//                NumberLiteralToken(5, 1, 5),
//            )
//
//        val result = rule.match(tokens)
//        assertTrue(result.isEmpty())
//    }
//
//    @Test
//    fun `SpaceAfterAssignationRule should fail when no space after equals`() {
//        val rule = SpaceAfterAssignationRule()
//        val tokens =
//            listOf(
//                VariableToken("variable", 1, 1),
//                WhiteSpaceToken(1, 2),
//                OperationToken(OperationEnum.EQUAL, 1, 3),
//                NumberLiteralToken(5, 1, 4), // No space after equals
//            )
//
//        val result = rule.match(tokens)
//        assertTrue(result.isNotEmpty())
//        assertTrue(result.any { it is NoSpaceAfterAssignationException })
//    }
//
//    @Test
//    fun `SpaceBeforeAssignationRule should pass when space exists before equals`() {
//        val rule = SpaceBeforeAssignationRule()
//        val tokens =
//            listOf(
//                VariableToken("variable", 1, 1),
//                WhiteSpaceToken(1, 2),
//                OperationToken(OperationEnum.EQUAL, 1, 3),
//                WhiteSpaceToken(1, 4),
//                NumberLiteralToken(5, 1, 5),
//            )
//
//        val result = rule.match(tokens)
//        assertTrue(result.isEmpty())
//    }
//
//    @Test
//    fun `SpaceBeforeAssignationRule should fail when no space before equals`() {
//        val rule = SpaceBeforeAssignationRule()
//        val tokens =
//            listOf(
//                VariableToken("variable", 1, 1),
//                OperationToken(OperationEnum.EQUAL, 1, 2), // No space before equals
//                WhiteSpaceToken(1, 3),
//                NumberLiteralToken(5, 1, 4),
//            )
//
//        val result = rule.match(tokens)
//        assertTrue(result.isNotEmpty())
//        assertTrue(result.any { it is NoSpaceBeforeAssignationException })
//    }
//
//    @Test
//    fun `NewLineAfterSemicolonRule should not throw when properly implemented`() {
//        val rule = LineJumpAfterSemicolonRule()
//        val tokens =
//            listOf(
//                VariableToken("variable", 1, 1),
//                EndSentenceToken(1, 2),
//            )
//
//        val result = rule.match(tokens)
//        assertTrue(result.isNotEmpty())
//        assertTrue(result.any { it is NoNewLineAfterSemiColon })
//    }
//
//    @Test
//    fun `combination of spacing rules should work together`() {
//        val spaceAfterColonRule = SpaceAfterColonRule()
//        val spaceBeforeColonRule = SpaceBeforeColonRule()
//        val spaceAfterAssignRule = SpaceAfterAssignationRule()
//        val spaceBeforeAssignRule = SpaceBeforeAssignationRule()
//
//        val validTokens =
//            listOf(
//                VariableToken("variable", 1, 1),
//                WhiteSpaceToken(1, 2),
//                TypeDeclaratorToken(1, 3),
//                WhiteSpaceToken(1, 4),
//                TypeToken(TypeEnum.STRING, 1, 5),
//                WhiteSpaceToken(1, 6),
//                OperationToken(OperationEnum.EQUAL, 1, 7),
//                WhiteSpaceToken(1, 8),
//                StringLiteralToken("value", 1, 9),
//            )
//
//        assertNull(spaceAfterColonRule.match(validTokens))
//        assertNull(spaceBeforeColonRule.match(validTokens))
//        assertNull(spaceAfterAssignRule.match(validTokens))
//        assertNull(spaceBeforeAssignRule.match(validTokens))
//    }
}
