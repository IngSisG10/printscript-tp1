package lexer.one.zero

import common.enums.OperationEnum
import common.token.OperationToken
import lexer.Lexer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LexerOperationTest {
    private fun lexOp(op: String): OperationToken {
        val tokens = Lexer().lex(op.byteInputStream())
        assertEquals(1, tokens.size, "Expected 1 token for '$op'")
        val t = tokens[0]
        assertTrue(t is OperationToken, "Expected OperationToken, got ${t::class.simpleName}")
        return t as OperationToken
    }

    @Test
    fun parsesPlus() {
        assertEquals(OperationEnum.SUM, lexOp("+").value)
    }

    @Test
    fun parsesMinus() {
        assertEquals(OperationEnum.MINUS, lexOp("-").value)
    }

    @Test
    fun parsesDivide() {
        assertEquals(OperationEnum.DIVIDE, lexOp("/").value)
    }

    @Test
    fun parsesMultiply() {
        assertEquals(OperationEnum.MULTIPLY, lexOp("*").value)
    }

    @Test
    fun parsesEqual() {
        assertEquals(OperationEnum.EQUAL, lexOp("=").value)
    }
}
