package token

import token.abs.TokenInterface

class PointToken(
    override val row: Int,
    override val position: Int
) : TokenInterface {
    override val name: String = "point"
    override val value: String = "."
}