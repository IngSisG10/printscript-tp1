package exception

class InvalidSnakeCaseException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("Invalid snake_case identifier", cause)
}
