package formatter

import common.enums.OperationEnum
import common.enums.TypeEnum
import common.token.NewLineToken
import common.token.NumberLiteralToken
import common.token.OperationToken
import common.token.TypeDeclaratorToken
import common.token.TypeToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import formatter.fixes.custom.MaxOneBlankLineFix
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FormmaterCustomTest {
    @Test
    fun `Should remove blank lines`() {
        val tokens =
            listOf(
                VariableToken("X", 1, 1),
                WhiteSpaceToken(1, 2),
                TypeDeclaratorToken(1, 3),
                WhiteSpaceToken(1, 4),
                TypeToken(TypeEnum.STRING, 1, 3),
                WhiteSpaceToken(1, 4),
                OperationToken(OperationEnum.EQUAL, 1, 5),
                WhiteSpaceToken(1, 6),
                NumberLiteralToken(3, 1, 7),
                NewLineToken(1, 7),
                NewLineToken(2, 8),
                NewLineToken(3, 9),
                VariableToken("Y", 1, 10),
                WhiteSpaceToken(1, 11),
                TypeDeclaratorToken(1, 12),
                WhiteSpaceToken(1, 13),
                TypeToken(TypeEnum.STRING, 1, 14),
                WhiteSpaceToken(1, 15),
                OperationToken(OperationEnum.EQUAL, 1, 16),
                WhiteSpaceToken(1, 17),
                NumberLiteralToken(3, 1, 18),
            ) // X : String = 3\n\n\nY: String = 3

        val formatter = Formatter(listOf(MaxOneBlankLineFix()))
        val result = formatter.format(tokens)
        assertEquals("X : string = 3\n\nY : string = 3", result)
    }

    @Test
    fun `Should leave as i`() {
        val tokens =
            listOf(
                VariableToken("X", 1, 1),
                WhiteSpaceToken(1, 2),
                TypeDeclaratorToken(1, 3),
                WhiteSpaceToken(1, 4),
                TypeToken(TypeEnum.STRING, 1, 3),
                WhiteSpaceToken(1, 4),
                OperationToken(OperationEnum.EQUAL, 1, 5),
                WhiteSpaceToken(1, 6),
                NumberLiteralToken(3, 1, 7),
                NewLineToken(1, 7),
                VariableToken("Y", 1, 10),
                WhiteSpaceToken(1, 11),
                TypeDeclaratorToken(1, 12),
                WhiteSpaceToken(1, 13),
                TypeToken(TypeEnum.STRING, 1, 14),
                WhiteSpaceToken(1, 15),
                OperationToken(OperationEnum.EQUAL, 1, 16),
                WhiteSpaceToken(1, 17),
                NumberLiteralToken(3, 1, 18),
            ) // X : String = 3\nY: String = 3

        val formatter = Formatter(listOf(MaxOneBlankLineFix()))
        val result = formatter.format(tokens)
        assertEquals("X : string = 3\nY : string = 3", result)
    }
}
