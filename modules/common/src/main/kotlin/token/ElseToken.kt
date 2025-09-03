package token

import token.abs.TokenInterface

class ElseToken(
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "else"
    override val value: String = "else"
}
