package common.exception

import common.token.abs.TokenPosition

class InvalidPascalCaseException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("Invalid PascalCase identifier", cause)
    constructor(position: TokenPosition) : this("Invalid PascalCase identifier at row ${position.row} and position ${position.pos}")
}
