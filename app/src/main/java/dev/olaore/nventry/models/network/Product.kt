package dev.olaore.nventry.models.network

data class Product(
    var id: String,
    var businessId: String,
    var name: String,
    var price: Float,
    var description: String,
    var sharingText: String,
    var category: String,
    var quantity: Int,
    var imageUrls: List<String>,
    val createdOn: Long
)