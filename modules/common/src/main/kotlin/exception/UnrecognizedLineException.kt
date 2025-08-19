package exception

class UnrecognizedLineException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No matching line pattern found", cause)
}