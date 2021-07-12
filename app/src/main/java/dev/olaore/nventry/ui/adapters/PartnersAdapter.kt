package dev.olaore.nventry.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.olaore.nventry.R
import dev.olaore.nventry.models.domain.Partner

class PartnersAdapter(
    private val ctx: Context,
    private val partners: List<Partner>
) : RecyclerView.Adapter<PartnersAdapter.PartnerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerViewHolder {
        return PartnerViewHolder(
            LayoutInflater.from(ctx).inflate(R.layout.item_partner, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PartnerViewHolder, position: Int) {
        val partner = this.partners[position]
        holder.bind(partner)
    }

    override fun getItemCount() = partners.size

    inner class PartnerViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {

        private val partnerName: TextView  = view.findViewById(R.id.partner_name)
        private val partnerEmail: TextView  = view.findViewById(R.id.partner_email)
        private val partnerImage: ImageView = view.findViewById(R.id.partner_image)

        fun bind(partner: Partner) {

            this.partnerName.text = partner.name
            this.partnerEmail.text = partner.email
            this.partnerImage.setImageResource(
                partner.image
            )

        }

    }

}