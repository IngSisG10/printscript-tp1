package common.token

import common.token.abs.TokenInterface

class NewLineToken(
    override val row: Int,
    override val position: Int,
) : common.token.abs.TokenInterface {
    override val name: String = "new_line"
    override val value: String = "\n"
}
