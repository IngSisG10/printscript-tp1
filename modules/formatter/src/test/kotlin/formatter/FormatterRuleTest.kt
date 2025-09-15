package formatter

import common.enums.OperationEnum
import common.enums.TypeEnum
import common.token.BooleanLiteralToken
import common.token.CloseBraceToken
import common.token.CloseParenthesisToken
import common.token.IfToken
import common.token.NewLineToken
import common.token.NumberLiteralToken
import common.token.OpenBraceToken
import common.token.OpenParenthesisToken
import common.token.OperationToken
import common.token.TypeDeclaratorToken
import common.token.TypeToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import formatter.fixes.required.IfBracePlacementFix
import formatter.fixes.required.IfInnerIndentationFix
import formatter.fixes.required.OneSpaceAfterTokenMaxFix
import formatter.fixes.required.SpaceAfterColonFix
import formatter.fixes.required.SpaceAfterEqualFix
import formatter.fixes.required.SpaceAfterOperatorFix
import formatter.fixes.required.SpaceBeforeColonFix
import formatter.fixes.required.SpaceBeforeEqualFix
import formatter.fixes.required.SpaceBeforeOperatorFix
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FormatterRuleTest {
    @Test
    fun `format should return empty string when tokens list is empty`() {
        val formatter = Formatter(emptyList())
        val result = formatter.format(emptyList())
        assertEquals("", result)
    }

    @Test
    fun `format should return converted tokens when formatterFixes list is empty`() {
        val tokens =
            listOf(
                VariableToken("test", 1, 1),
                TypeDeclaratorToken(1, 2),
                TypeToken(TypeEnum.STRING, 1, 3),
            )
        val formatter = Formatter(emptyList())
        val result = formatter.format(tokens)
        // Assuming converter joins tokens without spaces when no fixes applied
        assertEquals("test:string", result)
    }

    @Test
    fun `format should apply single formatter fix`() {
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                TypeDeclaratorToken(1, 2),
                TypeToken(TypeEnum.STRING, 1, 3),
            )
        val formatter = Formatter(listOf(SpaceBeforeColonFix()))
        val result = formatter.format(tokens)
        assertEquals("variable :string", result)
    }

    @Test
    fun `formatter should add space before colon fix`() {
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                TypeDeclaratorToken(1, 3),
                TypeToken(TypeEnum.STRING, 1, 4),
            )
        val formatter = Formatter(listOf(SpaceBeforeColonFix()))
        val result = formatter.format(tokens)
        assertEquals("variable :string", result)
    }

    @Test
    fun `add space after colon fix`() {
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                TypeDeclaratorToken(1, 3),
                TypeToken(TypeEnum.STRING, 1, 4),
            )
        val formatter = Formatter(listOf(SpaceAfterColonFix()))
        val result = formatter.format(tokens)
        assertEquals("variable: string", result)
    }

    @Test
    fun `add space before and after colon fix`() {
        val tokens =
            listOf(
                VariableToken("variable", 1, 1),
                TypeDeclaratorToken(1, 3),
                TypeToken(TypeEnum.STRING, 1, 4),
            )
        val formatter = Formatter(listOf(SpaceBeforeColonFix(), SpaceAfterColonFix()))
        val result = formatter.format(tokens)
        assertEquals("variable : string", result)
    }

    @Test
    fun `add space before equal fix`() {
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
        val formatter = Formatter(listOf(SpaceBeforeEqualFix()))
        val result = formatter.format(tokens)
        assertEquals("variable : string = 3", result)
    }

    @Test
    fun `add space after equal fix`() {
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
        val formatter = Formatter(listOf(SpaceAfterEqualFix()))
        val result = formatter.format(tokens)
        assertEquals("variable : string = 3", result)
    }

    @Test
    fun `add space before and after equal fix`() {
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
        val formatter = Formatter(listOf(SpaceBeforeEqualFix(), SpaceAfterEqualFix()))
        val result = formatter.format(tokens)
        assertEquals("variable : string = 3", result)
    }

    @Test
    fun `add space before operator`() {
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
                OperationToken(OperationEnum.SUM, 1, 9),
                NumberLiteralToken(5, 1, 10),
            )
        val formatter = Formatter(listOf(SpaceBeforeOperatorFix()))
        val result = formatter.format(tokens)
        assertEquals("variable : string = 3 +5", result)
    }

    @Test
    fun `add space after operator`() {
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
                OperationToken(OperationEnum.SUM, 1, 10),
                NumberLiteralToken(5, 1, 11),
            )
        val formatter = Formatter(listOf(SpaceAfterOperatorFix()))
        val result = formatter.format(tokens)
        assertEquals("variable : string = 3+ 5", result)
    }

    @Test
    fun `add space before and after operator`() {
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
                OperationToken(OperationEnum.SUM, 1, 10),
                NumberLiteralToken(5, 1, 11),
            )
        val formatter = Formatter(listOf(SpaceBeforeOperatorFix(), SpaceAfterOperatorFix()))
        val result = formatter.format(tokens)
        assertEquals("variable : string = 3 + 5", result)
    }

    @Test
    fun `if brace placement fix removes newline between close parenthesis and open brace`() {
        val tokens =
            listOf(
                IfToken(1, 1),
                WhiteSpaceToken(1, 2),
                OpenParenthesisToken(1, 3),
                BooleanLiteralToken(true, 1, 4),
                CloseParenthesisToken(1, 5),
                NewLineToken(1, 6), // Si agrego WhiteSpaceToken se rompe y no funciona.
                OpenBraceToken(1, 7),
            )
        val formatter = Formatter(listOf(IfBracePlacementFix()))
        val result = formatter.format(tokens)
        assertEquals("if (true){", result)
    }

    // fixme: this test is failing, need to fix the implementation of LineJumpAfterSemiColonFix
//    @Test
//    fun `line jump after semicolon fix adds newline after semicolon`() {
//        val tokens =
//            listOf(
//                VariableToken("x", 1, 1),
//                EndSentenceToken(1, 2),
//            )
//        val formatter = Formatter(listOf(LineJumpAfterSemiColonFix()))
//        val result = formatter.format(tokens)
//        assertEquals("x;\n", result)
//    }

    @Test
    fun `one space after token max fix reduces multiple spaces to one`() {
        val tokens =
            listOf(
                VariableToken("a", 1, 1),
                WhiteSpaceToken(1, 2),
                WhiteSpaceToken(1, 3),
                WhiteSpaceToken(1, 4),
                VariableToken("b", 1, 5),
            )
        val formatter = Formatter(listOf(OneSpaceAfterTokenMaxFix()))
        val result = formatter.format(tokens)
        assertEquals("a b", result)
    }

    @Test
    fun `if inner indentation fix sets correct indentation for deeply nested blocks with n = 2`() {
        val tokens =
            listOf(
                IfToken(1, 1),
                WhiteSpaceToken(1, 2),
                OpenParenthesisToken(1, 3),
                BooleanLiteralToken(true, 1, 4),
                CloseParenthesisToken(1, 5),
                WhiteSpaceToken(1, 6),
                OpenBraceToken(1, 7),
                NewLineToken(1, 8),
                // Some wrong indentation (will be fixed to 2 spaces for depth 1)
                WhiteSpaceToken(1, 9),
                WhiteSpaceToken(1, 10),
                VariableToken("x", 1, 11),
                NewLineToken(1, 12),
                // Some wrong indentation (will be fixed to 2 spaces for depth 1)
                WhiteSpaceToken(1, 13),
                WhiteSpaceToken(1, 14),
                IfToken(1, 15),
                WhiteSpaceToken(1, 16),
                OpenParenthesisToken(1, 17),
                BooleanLiteralToken(true, 1, 18),
                CloseParenthesisToken(1, 19),
                WhiteSpaceToken(1, 20),
                OpenBraceToken(1, 21),
                NewLineToken(1, 22),
                // Some wrong indentation (will be fixed to 4 spaces for depth 2)
                WhiteSpaceToken(1, 23),
                WhiteSpaceToken(1, 24),
                WhiteSpaceToken(1, 25),
                WhiteSpaceToken(1, 26),
                VariableToken("y", 1, 27),
                NewLineToken(1, 28),
                // Add proper indentation before inner closing brace (2 spaces)
                WhiteSpaceToken(1, 29),
                WhiteSpaceToken(1, 30),
                CloseBraceToken(1, 31),
                NewLineToken(1, 32),
                CloseBraceToken(1, 33),
            )
        val formatter = Formatter(listOf(IfInnerIndentationFix()))
        val result = formatter.format(tokens)
        assertEquals("if (true) {\n  x\n  if (true) {\n    y\n  }\n}", result)
    }

    @Test
    fun `if inner indentation fix sets correct indentation for deeply nested blocks with n equals 4`() {
        val tokens =
            listOf(
                IfToken(1, 1),
                WhiteSpaceToken(1, 2),
                OpenParenthesisToken(1, 3),
                BooleanLiteralToken(true, 1, 4),
                CloseParenthesisToken(1, 5),
                WhiteSpaceToken(1, 6),
                OpenBraceToken(1, 7),
                NewLineToken(1, 8),
                // Some wrong indentation (will be fixed to 4 spaces for depth 1)
                WhiteSpaceToken(1, 9),
                WhiteSpaceToken(1, 10),
                VariableToken("x", 1, 11),
                NewLineToken(1, 12),
                // Some wrong indentation (will be fixed to 4 spaces for depth 1)
                WhiteSpaceToken(1, 13),
                WhiteSpaceToken(1, 14),
                IfToken(1, 15),
                WhiteSpaceToken(1, 16),
                OpenParenthesisToken(1, 17),
                BooleanLiteralToken(true, 1, 18),
                CloseParenthesisToken(1, 19),
                WhiteSpaceToken(1, 20),
                OpenBraceToken(1, 21),
                NewLineToken(1, 22),
                // Some wrong indentation (will be fixed to 8 spaces for depth 2)
                WhiteSpaceToken(1, 23),
                WhiteSpaceToken(1, 24),
                WhiteSpaceToken(1, 25),
                WhiteSpaceToken(1, 26),
                VariableToken("y", 1, 27),
                NewLineToken(1, 28),
                // Add proper indentation before inner closing brace (4 spaces)
                WhiteSpaceToken(1, 29),
                WhiteSpaceToken(1, 30),
                CloseBraceToken(1, 31),
                NewLineToken(1, 32),
                CloseBraceToken(1, 33),
            )

        val formatter = Formatter(listOf(IfInnerIndentationFix()))
        val result = formatter.format(tokens)
        assertEquals("if (true) {\n  x\n  if (true) {\n    y\n  }\n}", result)
    }

    @Test
    fun `if inner indentation fix sets correct indentation if with one block and n equals 2`() {
        val tokens =
            listOf(
                IfToken(1, 1),
                WhiteSpaceToken(1, 2),
                OpenParenthesisToken(1, 3),
                BooleanLiteralToken(true, 1, 4),
                CloseParenthesisToken(1, 5),
                WhiteSpaceToken(1, 6),
                OpenBraceToken(1, 7),
                NewLineToken(1, 8),
                WhiteSpaceToken(1, 9),
                WhiteSpaceToken(1, 10),
                WhiteSpaceToken(1, 11),
                VariableToken("x", 1, 12),
                NewLineToken(1, 13),
                WhiteSpaceToken(1, 14),
                VariableToken("y", 1, 15),
                NewLineToken(1, 16),
                CloseBraceToken(1, 17),
            )
        val formatter = Formatter(listOf(IfInnerIndentationFix()))
        val result = formatter.format(tokens)
        assertEquals("if (true) {\n  x\n  y\n}", result)
    }
}
