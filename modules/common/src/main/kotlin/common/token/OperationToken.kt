package common.token

import common.enums.OperationEnum
import common.token.abs.OperationInterface
import common.token.abs.TokenInterface

class OperationToken(
    override val value: OperationEnum,
    override val row: Int,
    override val position: Int,
) : TokenInterface,
    OperationInterface {
    override val name: String = "operation"
    override val priority: Int = evaluatePriority()

    private fun evaluatePriority(): Int =
        when (value) {
            OperationEnum.MINUS -> 9
            OperationEnum.SUM -> 8
            OperationEnum.MULTIPLY -> 7
            OperationEnum.DIVIDE -> 6
            else -> 0
        }
}
