package common.token

import common.token.abs.OperationInterface
import common.token.abs.TokenInterface

class NumberLiteralToken(
    override val value: Number,
    override val row: Int,
    override val position: Int,
) : TokenInterface,
    OperationInterface {
    override val name: String = "number_literal"
    override val priority: Int = 0
}
