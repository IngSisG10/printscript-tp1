package common.token

import common.enums.FunctionEnum
import common.token.abs.TokenInterface

class FunctionToken(
    override val value: common.enums.FunctionEnum,
    override val row: Int,
    override val position: Int,
) : common.token.abs.TokenInterface {
    override val name: String = "function"
}
