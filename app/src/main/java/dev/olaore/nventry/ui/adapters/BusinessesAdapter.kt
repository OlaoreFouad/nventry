package dev.olaore.nventry.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

        fun bind(business: Business) {

            businessName.text = business.name
            Glide.with(ctx).load(business.logoUrl).placeholder(R.drawable.image_loading_gif).into(businessImage)

        }

    }

}