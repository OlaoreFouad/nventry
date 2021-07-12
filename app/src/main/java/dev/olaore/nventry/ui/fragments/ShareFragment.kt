package dev.olaore.nventry.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentShareBinding
import dev.olaore.nventry.models.database.SharedProduct
import dev.olaore.nventry.ui.viewmodels.ShareViewModel
import dev.olaore.nventry.utils.obtainViewModel
import dev.olaore.nventry.utils.shareProduct
import java.text.SimpleDateFormat
import java.util.*

class ShareFragment : Fragment() {

    private lateinit var binding: FragmentShareBinding
    private lateinit var viewModel: ShareViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentShareBinding.inflate(inflater)
        viewModel = obtainViewModel(ShareViewModel::class.java)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // subscribe for sharedproducts event
        viewModel.sharedProducts.observe(viewLifecycleOwner, Observer { products ->
            // populate the list
            binding.sharedProductsList.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = SharedProductsAdapter(requireContext(), products) { text, title, imageUrl, imageView ->
                    shareProduct(
                        requireContext(), text, imageView, title, imageUrl
                    )
                }
            }
        })

        // get shared products
        viewModel.getSharedProducts()
    }

}

class SharedProductsAdapter(
    private val ctx: Context,
    private val sharedProducts: List<SharedProduct>,
    private val onShareClicked: (text: String, title: String, imageUrl: String, ImageView) -> Unit
) : RecyclerView.Adapter<SharedProductsAdapter.SharedProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedProductViewHolder =
        SharedProductViewHolder(
            LayoutInflater.from(ctx).inflate(R.layout.item_shared_product, parent, false)
        )

    override fun onBindViewHolder(holder: SharedProductViewHolder, position: Int) {
        val product = sharedProducts[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return sharedProducts.size
    }

    inner class SharedProductViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {

        private val productName: TextView = view.findViewById(R.id.shared_product_name)
        private val productSharingText: TextView =
            view.findViewById(R.id.shared_product_sharing_text)
        private val productImage: ImageView = view.findViewById(R.id.shared_product_image)
        private val productSharedAt: TextView = view.findViewById(R.id.shared_product_timestamp)
        private val share: ImageView = view.findViewById(R.id.share_product_action)

        fun bind(product: SharedProduct) {

            productName.text = product.name.toUpperCase()
            productSharingText.text = product.sharingText
            Glide.with(ctx).load(product.image).into(productImage)

            val sharedAtDate = SimpleDateFormat("E, HH:mm").format(Date(product.sharedAt))
            productSharedAt.text = sharedAtDate

            share.setOnClickListener {
                onShareClicked(
                    product.sharingText, "Share ${product.name}", product.image, productImage
                )
            }

        }

    }

}