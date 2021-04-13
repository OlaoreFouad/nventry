package dev.olaore.nventry.models.domain

data class User(
    var userId: String,
    var username: String,
    var email: String,
    var createdOn: String,
    var businesses: List<String>
)