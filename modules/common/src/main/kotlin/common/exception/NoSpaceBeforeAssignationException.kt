package exception

import common.token.abs.TokenPosition

class NoSpaceBeforeAssignationException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No space before assignation operator '='", cause)
    constructor(position: TokenPosition) : this("No space before assignation operator at row ${position.row} and position ${position.pos}")
}
