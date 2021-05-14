package dev.olaore.nventry.models.domain

import dev.olaore.nventry.models.network.NetworkBusiness
import java.io.Serializable

data class Business(
    val businessId: String,
    val userId: String,
    var name: String,
    var logoUrl: String,
    var description: String
) : Serializable {

    constructor(networkBusiness: NetworkBusiness) : this(
        networkBusiness.businessId,
        networkBusiness.userId,
        networkBusiness.name,
        networkBusiness.logoUrl,
        networkBusiness.description
    )

}