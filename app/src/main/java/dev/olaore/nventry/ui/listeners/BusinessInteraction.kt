package dev.olaore.nventry.ui.listeners

import dev.olaore.nventry.models.domain.Business

interface BusinessInteraction {

    fun onBusinessClicked(businessId: String)
    fun onEditBusiness(business: Business)
    fun onDeleteBusiness(businessId: String)

}