package common.exception

import common.token.abs.TokenPosition

class InvalidCamelCaseException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("Invalid camelCase identifier", cause)
    constructor(position: TokenPosition) : this("Invalid camelCase identifier at row ${position.row} and position ${position.pos}")
}
