package dev.olaore.nventry.models.network

data class NetworkUser (
    var userId: String = "",
    var username: String = "",
    var email: String = "",
    var createdOn: Long = 0L,
    var businesses: List<String> = listOf()
)