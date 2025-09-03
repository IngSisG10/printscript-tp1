package common.exception

class UnknownExpressionException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("Unknown expression found", cause)
}
