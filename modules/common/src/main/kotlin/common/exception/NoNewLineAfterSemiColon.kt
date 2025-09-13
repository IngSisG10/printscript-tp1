package common.exception

import common.token.abs.TokenPosition

class NoNewLineAfterSemiColon(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No new line after Semi Colon", cause)
    constructor(position: TokenPosition) : this("No new line after Semi Colon at row ${position.row} and position ${position.pos}")
}
