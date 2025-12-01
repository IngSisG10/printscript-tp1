package linter.dto

data class LinterDto(
    val name: String,
    val data: List<DataItem>,
)

data class DataItem(
    val value: String,
    val default: String,
    val type: String,
)
