package common.exception

class InvalidPascalCaseException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("Invalid PascalCase identifier", cause)
}
