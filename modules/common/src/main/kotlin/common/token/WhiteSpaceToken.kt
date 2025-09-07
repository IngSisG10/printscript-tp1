package common.token

import common.token.abs.TokenInterface

class WhiteSpaceToken(
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "white_space"
    override val value: String = " "
}
