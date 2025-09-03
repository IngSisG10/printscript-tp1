package exception

class NoSpaceAfterColonException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No space after colon", cause)
}
