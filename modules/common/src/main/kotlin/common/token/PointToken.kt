package common.token

import common.token.abs.TokenInterface

class PointToken(
    override val row: Int,
    override val position: Int,
) : common.token.abs.TokenInterface {
    override val name: String = "point"
    override val value: String = "."
}
