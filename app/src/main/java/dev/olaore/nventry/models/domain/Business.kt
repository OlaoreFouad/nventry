package dev.olaore.nventry.models.domain

import dev.olaore.nventry.models.network.NetworkBusiness

data class Business(
    val businessId: String,
    val userId: String,
    var name: String,
    var logoUrl: String
) {

    constructor(networkBusiness: NetworkBusiness) : this(
        networkBusiness.businessId,
        networkBusiness.userId,
        networkBusiness.name,
        networkBusiness.logoUrl
    )

}