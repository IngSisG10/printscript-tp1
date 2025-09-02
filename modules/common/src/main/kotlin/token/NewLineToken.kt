package token

import token.abs.TokenInterface

class NewLineToken(
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "new_line"
    override val value: String = "\n"
}
