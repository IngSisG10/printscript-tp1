package common.token

import common.token.abs.OperationInterface
import common.token.abs.TokenInterface

class OpenParenthesisToken(
    override val row: Int,
    override val position: Int,
) : TokenInterface,
    OperationInterface {
    override val name: String = "open_parenthesis"
    override val value: String = "("
    override val priority: Int = 2
}
