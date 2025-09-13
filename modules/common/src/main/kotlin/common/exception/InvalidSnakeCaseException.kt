package common.exception

import common.token.abs.TokenPosition

class InvalidSnakeCaseException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("Invalid snake_case identifier", cause)
    constructor(position: TokenPosition) : this("Invalid snake_case identifier at row ${position.row} and position ${position.pos}")
}
