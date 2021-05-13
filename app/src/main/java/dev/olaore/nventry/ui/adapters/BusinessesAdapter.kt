package dev.olaore.nventry.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.olaore.nventry.R
import dev.olaore.nventry.models.domain.Business
import dev.olaore.nventry.ui.listeners.BusinessInteraction

class BusinessesAdapter(
    private val ctx: Context,
    private var businesses: List<Business>,
    private val businessInteraction: BusinessInteraction
) : RecyclerView.Adapter<BusinessesAdapter.BusinessViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BusinessesAdapter.BusinessViewHolder {

        return BusinessViewHolder(
            LayoutInflater.from(ctx).inflate(R.layout.item_business, parent, false)
        )

    }

    override fun onBindViewHolder(holder: BusinessesAdapter.BusinessViewHolder, position: Int) {
        val business = this.businesses[position]
        holder.bind(business)
    }

    override fun getItemCount() = businesses.size

    fun setList(businesses: List<Business>) {
        this.businesses = businesses;
        this.notifyDataSetChanged()
    }

    inner class BusinessViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {

        private val businessName: TextView = view.findViewById(R.id.business_name)
        private val businessImage: ImageView = view.findViewById(R.id.business_image)
        private val more: ImageView = view.findViewById(R.id.more_image)

        fun bind(business: Business) {

            view.setOnClickListener {
                businessInteraction.onBusinessClicked(business.businessId)
            }

            businessName.text = business.name
            Glide.with(ctx).load(business.logoUrl).placeholder(R.drawable.no_image).into(businessImage)

            more.setOnClickListener {
                val popupMenu = PopupMenu(ctx, more)
                popupMenu.menuInflater.inflate(R.menu.item_business_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.business_edit_menu_item -> {
                            businessInteraction.onEditBusiness(business.businessId)
                            true
                        }
                        R.id.business_delete_menu_item -> {
                            businessInteraction.onDeleteBusiness(business.businessId)
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }

        }

    }

}