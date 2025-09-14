package common.exception

import common.token.abs.TokenPosition

class NoSpaceBeforeColonException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No space before colon", cause)
    constructor(position: TokenPosition) : this("No space before colon at row ${position.row} and position ${position.pos}")
}
