package common.exception

class InvalidIfBracePlacementException (
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("Invalid if brace placement", cause)
}