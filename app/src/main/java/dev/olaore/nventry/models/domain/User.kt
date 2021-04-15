package dev.olaore.nventry.models.domain

data class User(
    var userId: String,
    var username: String,
    var email: String,
    var createdOn: Long,
    var businesses: List<String>,
    var password: String
)

data class TemporaryUser(
    var username: String,
    var password: String,
    var email: String,
    var createdOn: Long
)