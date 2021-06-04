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
import dev.olaore.nventry.models.network.Product
import dev.olaore.nventry.ui.listeners.BusinessInteraction

class ProductsAdapter(
    private val ctx: Context,
    private var products: List<Product>,
    private val productClicked: (String) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsAdapter.ProductViewHolder {

        return ProductViewHolder(
            LayoutInflater.from(ctx).inflate(R.layout.item_product, parent, false)
        )

    }

    override fun onBindViewHolder(holder: ProductsAdapter.ProductViewHolder, position: Int) {
        val product = this.products[position]
        holder.bind(product)
    }

    override fun getItemCount() = products.size

    inner class ProductViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {

        private val productName: TextView = view.findViewById(R.id.product_name)
        private val productPrice: TextView = view.findViewById(R.id.product_price)
        private val productImage: ImageView = view.findViewById(R.id.product_image)

        fun bind(product: Product) {

            view.setOnClickListener {
                productClicked(product.id)
            }

            productName.text = product.name
            productPrice.text = ctx.resources.getString(R.string.product_price_text, product.price.toString())
            Glide.with(ctx).load(product.imageUrls[0]).placeholder(R.drawable.no_image).into(productImage)

        }

    }

}