package common.exception

import common.token.abs.TokenPosition

class InvalidPrintLnArgumentException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("Invalid argument in println function", cause)
    constructor(position: TokenPosition) : this("Invalid argument in println function at row ${position.row} and position ${position.pos}")
}
