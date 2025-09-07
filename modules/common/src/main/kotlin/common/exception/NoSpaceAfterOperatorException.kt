package exception

class NoSpaceAfterOperatorException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No space after operator", cause)
}
