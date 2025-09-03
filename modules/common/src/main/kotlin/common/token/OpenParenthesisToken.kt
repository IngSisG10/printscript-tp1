package common.token

import common.token.abs.TokenInterface

class OpenParenthesisToken(
    override val row: Int,
    override val position: Int,
) : common.token.abs.TokenInterface {
    override val name: String = "open_parenthesis"
    override val value: String = "("
}
