package common.token

import common.enums.OperationEnum
import common.token.abs.TokenInterface

class OperationToken(
    override val value: OperationEnum,
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "operation"
}
