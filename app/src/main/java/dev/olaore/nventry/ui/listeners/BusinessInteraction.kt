package dev.olaore.nventry.ui.listeners

interface BusinessInteraction {

    fun onBusinessClicked(businessId: String)
    fun onEditBusiness(businessId: String)
    fun onDeleteBusiness(businessId: String)

}