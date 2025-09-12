package common.exception

class InvalidPrintLnArgumentException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("Invalid argument in println function", cause)
}
