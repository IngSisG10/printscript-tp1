package common.token

import common.enums.TypeEnum
import common.token.abs.TokenInterface

class TypeToken(
    override val value: TypeEnum,
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "type"
}
