package exception

class MoreThanOneSpaceAfterTokenException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("More than one space after token.", cause)
}
