package exception

import common.token.abs.TokenPosition

class NoSpaceAfterAssignationException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No space after assignation operator '='", cause)
    constructor(position: TokenPosition) : this("No space after assignation operator at row ${position.row} and position ${position.pos}")
}
