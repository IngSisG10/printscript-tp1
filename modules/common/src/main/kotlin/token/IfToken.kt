package token

import token.abs.TokenInterface

class IfToken(
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "if"
    override val value: String = "if"
}
