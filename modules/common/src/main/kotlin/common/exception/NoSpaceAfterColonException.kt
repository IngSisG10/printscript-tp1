package common.exception

import common.token.abs.TokenPosition

class NoSpaceAfterColonException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No space after colon", cause)
    constructor(position: TokenPosition) : this("No space after colon at row ${position.row} and position ${position.pos}")
}
