package lexer

import common.enums.OperationEnum
import common.token.OperationToken
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LexerOperationTest {
    private fun lexOp(op: String): common.token.OperationToken {
        val tokens = Lexer().lex(op)
        assertEquals(1, tokens.size, "Expected 1 token for '$op'")
        val t = tokens[0]
        assertTrue(t is common.token.OperationToken, "Expected OperationToken, got ${t::class.simpleName}")
        return t as common.token.OperationToken
    }

    @Test
    fun parsesPlus() {
        assertEquals(common.enums.OperationEnum.SUM, lexOp("+").value)
    }

    @Test
    fun parsesMinus() {
        assertEquals(common.enums.OperationEnum.MINUS, lexOp("-").value)
    }

    @Test
    fun parsesDivide() {
        assertEquals(common.enums.OperationEnum.DIVIDE, lexOp("/").value)
    }

    @Test
    fun parsesMultiply() {
        assertEquals(common.enums.OperationEnum.MULTIPLY, lexOp("*").value)
    }

    @Test
    fun parsesEqual() {
        assertEquals(common.enums.OperationEnum.EQUAL, lexOp("=").value)
    }
}
