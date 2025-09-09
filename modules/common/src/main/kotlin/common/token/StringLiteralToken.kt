package common.token

import common.token.abs.OperationInterface
import common.token.abs.TokenInterface

class StringLiteralToken(
    override val value: String,
    override val row: Int,
    override val position: Int, // position que tiene en la linea
) : TokenInterface,
    OperationInterface {
    override val name: String = "string_literal"
    override val priority: Int = 0
}
