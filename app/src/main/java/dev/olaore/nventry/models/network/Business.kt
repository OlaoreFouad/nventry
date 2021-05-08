package dev.olaore.nventry.models.network

data class NetworkBusiness(
    var businessId: String = "",
    var userId: String = "",
    var name: String = "",
    var description: String = "",
    var logoUrl: String = "",
    var partnerIds: List<String> = listOf(),
    var businessSetting: NetworkBusinessSetting? = NetworkBusinessSetting()
)

data class NetworkBusinessSetting(
    var recentlySharedProducts: List<String> = listOf()
)

