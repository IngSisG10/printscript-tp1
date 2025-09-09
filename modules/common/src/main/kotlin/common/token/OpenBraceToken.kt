package common.token

import common.token.abs.TokenInterface

class OpenBraceToken(
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "open_brace"
    override val value: String = "{"
}
