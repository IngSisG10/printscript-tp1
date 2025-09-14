package common.exception

class TooManyBlankLinesException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("Too many blank lines", cause)
}
