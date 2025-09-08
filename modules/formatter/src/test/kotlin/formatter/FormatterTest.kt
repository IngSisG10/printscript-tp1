package formatter

import common.enums.OperationEnum
import common.enums.TypeEnum
import common.token.NumberLiteralToken
import common.token.OperationToken
import common.token.TypeDeclaratorToken
import common.token.TypeToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import linter.Linter
import linter.syntax.rules.SpaceAfterAssignationRule
import linter.syntax.rules.SpaceAfterColonRule
import linter.syntax.rules.SpaceBeforeAssignationRule
import linter.syntax.rules.SpaceBeforeColonRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FormatterTest {
    @Test
    fun `Formatter should add space after colon`() {
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                WhiteSpaceToken(1, 2),
                TypeDeclaratorToken(1, 3),
                TypeToken(TypeEnum.STRING, 1, 4),
            )
        val linter = Linter(listOf(SpaceAfterColonRule()))
        val formatter = Formatter(linter)

        val result = formatter.format(tokens)

        assertEquals("variable : String", result)
    }

    @Test
    fun `Formatter should add space before colon`() {
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                TypeDeclaratorToken(1, 2),
                WhiteSpaceToken(1, 3),
                TypeToken(TypeEnum.STRING, 1, 4),
            )

        val linter = Linter(listOf(SpaceBeforeColonRule()))
        val formatter = Formatter(linter)

        val result = formatter.format(tokens)

        assertEquals("variable : String", result)
    }

    @Test
    fun `Formatter should add space before and after colon`() {
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                TypeDeclaratorToken(1, 2),
                TypeToken(TypeEnum.STRING, 1, 4),
            )

        val linter = Linter(listOf(SpaceBeforeColonRule(), SpaceAfterColonRule()))
        val formatter = Formatter(linter)

        val result = formatter.format(tokens)

        assertEquals("variable : String", result)
    }

    @Test
    fun `Formatter should not add space before and after colon`() {
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                TypeDeclaratorToken(1, 2),
                TypeToken(TypeEnum.STRING, 1, 3),
            )

        val linter = Linter(listOf(SpaceAfterAssignationRule()))
        val formatter = Formatter(linter)

        val result = formatter.format(tokens)

        assertEquals("variable:String", result)
    }

    @Test
    fun `Formatter should add space before assignator`() {
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                WhiteSpaceToken(1, 2),
                TypeDeclaratorToken(1, 3),
                WhiteSpaceToken(1, 4),
                TypeToken(TypeEnum.STRING, 1, 5),
                OperationToken(OperationEnum.EQUAL, 1, 6),
                WhiteSpaceToken(1, 7),
                NumberLiteralToken(3, 1, 8),
            )

        val linter = Linter(listOf(SpaceBeforeAssignationRule()))
        val formatter = Formatter(linter)

        val result = formatter.format(tokens)

        assertEquals("variable : String = 3", result)
    }

    @Test
    fun `Formatter should add space after assignator`() {
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                WhiteSpaceToken(1, 2),
                TypeDeclaratorToken(1, 3),
                WhiteSpaceToken(1, 4),
                TypeToken(TypeEnum.STRING, 1, 5),
                WhiteSpaceToken(1, 6),
                OperationToken(OperationEnum.EQUAL, 1, 7),
                NumberLiteralToken(3, 1, 8),
            )

        val linter = Linter(listOf(SpaceAfterAssignationRule()))
        val formatter = Formatter(linter)

        val result = formatter.format(tokens)

        assertEquals("variable : String = 3", result)
    }

    @Test
    fun `Formatter should add space before and after assignator`() {
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                WhiteSpaceToken(1, 2),
                TypeDeclaratorToken(1, 3),
                WhiteSpaceToken(1, 4),
                TypeToken(TypeEnum.STRING, 1, 5),
                OperationToken(OperationEnum.EQUAL, 1, 6),
                NumberLiteralToken(3, 1, 7),
            )

        val linter = Linter(listOf(SpaceBeforeAssignationRule(), SpaceAfterAssignationRule()))
        val formatter = Formatter(linter)

        val result = formatter.format(tokens)

        assertEquals("variable : String = 3", result)
    }

    @Test
    fun `Formatter should not add space at all before and after assignator`() {
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                WhiteSpaceToken(1, 2),
                TypeDeclaratorToken(1, 3),
                WhiteSpaceToken(1, 4),
                TypeToken(TypeEnum.STRING, 1, 5),
                WhiteSpaceToken(1, 6),
                OperationToken(OperationEnum.EQUAL, 1, 7),
                WhiteSpaceToken(1, 8),
                NumberLiteralToken(3, 1, 9),
            )

        val linter = Linter(listOf(SpaceBeforeAssignationRule(), SpaceAfterAssignationRule()))
        val formatter = Formatter(linter)

        val result = formatter.format(tokens)

        assertEquals("variable : String = 3", result)
    }

//    @Test
//    fun `trims newlines before println`() {
//        val tokens =
//            listOf(
//                VariableToken("x", 1, 1),
//                NewLineToken(1, 2),
//                NewLineToken(2, 1),
//                NewLineToken(3, 1),
//                FunctionToken(FunctionEnum.PRINTLN, 4, 1),
//            )
//        val linter = Linter(listOf(NewLineBeforePrintlnRule()))
//        val formatter = Formatter(linter)
//        val result = formatter.format(tokens)
//        val expected = "x"
//
//        assertEquals(expected, result) // No corre correctamente.
//    }
//
//    @Test
//    fun `trims newlines before operator`() {
//        val tokens =
//            listOf(
//                VariableToken("variable", 1, 1),
//                WhiteSpaceToken(1, 2),
//                TypeDeclaratorToken(1, 3),
//                WhiteSpaceToken(1, 4),
//                TypeToken(TypeEnum.STRING, 1, 5),
//                WhiteSpaceToken(1, 6),
//                OperationToken(OperationEnum.EQUAL, 1, 7),
//                WhiteSpaceToken(1, 8),
//                NumberLiteralToken(3, 1, 9),
//            )
//    }
}
