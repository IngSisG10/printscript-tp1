package exception

import common.token.abs.TokenPosition

class NoSpaceAfterOperatorException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No space after operator", cause)
    constructor(position: TokenPosition) : this("\"No space after operator at row ${position.row} and position ${position.pos}")
}
