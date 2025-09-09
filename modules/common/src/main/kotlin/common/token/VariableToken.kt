package common.token

import common.token.abs.OperationInterface
import common.token.abs.TokenInterface

class VariableToken(
    override val value: String,
    override val row: Int,
    override val position: Int,
) : TokenInterface,
    OperationInterface {
    override val name: String = "variable"
    override val priority: Int = 0
}
