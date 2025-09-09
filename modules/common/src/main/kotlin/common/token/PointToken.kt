package common.token

import common.token.abs.TokenInterface

// Fixme: Delete this and implement Numbers with points
class PointToken(
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "point"
    override val value: String = "."
}
