package dev.olaore.nventry.models.network

data class Product(
    var id: String = "",
    var businessId: String = "",
    var name: String= "",
    var price: Float = 0F,
    var description: String = "",
    var sharingText: String = "",
    var category: String = "",
    var quantity: Int = 0,
    var imageUrls: List<String> = listOf(),
    val createdOn: Long = 0L,
    val webLink: String = ""
)