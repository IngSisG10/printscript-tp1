package common.token

import common.enums.TypeEnum
import common.token.abs.TokenInterface

class TypeToken(
    override val value: common.enums.TypeEnum,
    override val row: Int,
    override val position: Int,
) : common.token.abs.TokenInterface {
    override val name: String = "type"
}
