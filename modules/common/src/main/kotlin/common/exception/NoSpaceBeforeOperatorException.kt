package exception

import common.token.abs.TokenPosition
import exception.NoSpaceAfterOperatorException

class NoSpaceBeforeOperatorException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No space before operator", cause)
    constructor(position: TokenPosition) : this("No space before operator at row ${position.row} and position ${position.pos}")
}
