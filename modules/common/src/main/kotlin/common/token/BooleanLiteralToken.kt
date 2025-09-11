package common.token

import common.token.abs.OperationInterface
import common.token.abs.TokenInterface

class BooleanLiteralToken(
    override val value: Boolean,
    override val row: Int,
    override val position: Int,
) : TokenInterface,
    OperationInterface {
    override val priority: Int = 2
    override val name: String = "boolean_literal"
}
