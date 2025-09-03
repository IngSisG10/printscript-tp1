package common.exception

class NoMatchingQuotationMarksException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No matching quotation marks found", cause)
}
