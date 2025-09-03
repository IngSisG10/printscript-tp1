package token

import token.abs.TokenInterface

class OpenParenthesisToken(
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "open_parenthesis"
    override val value: String = "("
}
