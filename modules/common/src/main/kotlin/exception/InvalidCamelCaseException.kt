package exception

class InvalidCamelCaseException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("Invalid camelCase identifier", cause)
}
