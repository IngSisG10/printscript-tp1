package exception

class NoSpaceAfterAssignationException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No space after assignation operator '='", cause)
}
