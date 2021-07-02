package dev.olaore.nventry.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.olaore.nventry.R


class SelectImagesAdapter(
    private var ctx: Context,
    private var images: List<String>
) : RecyclerView.Adapter<SelectImagesAdapter.ImageViewHolder>() {

    var selectedImageIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(ctx).inflate(R.layout.item_select_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val url = this.images[position]
        holder.bind(url)

    }

    override fun getItemCount() = images.size

    inner class ImageViewHolder(_v: View) : RecyclerView.ViewHolder(_v) {

        private var actualImage: ImageView = _v.findViewById(R.id.actual_image)
        private var checkImage: ImageView = _v.findViewById(R.id.check_image)

        init {
            _v.setOnClickListener {
                if (selectedImageIndex == adapterPosition) {
                    return@setOnClickListener
                } else {
                    selectedImageIndex = adapterPosition
                    notifyDataSetChanged()
                }
            }
        }

        fun bind(url: String) {
            Glide.with(ctx).load(url).into(actualImage)

            checkImage.visibility =
                if (selectedImageIndex == adapterPosition) View.VISIBLE else View.GONE

        }

    }

}