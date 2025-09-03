package token

import token.abs.TokenInterface

class CloseParenthesisToken(
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "close_parenthesis"
    override val value: String = ")"
}
