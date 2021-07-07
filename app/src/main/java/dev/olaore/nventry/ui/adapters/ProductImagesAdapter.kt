package dev.olaore.nventry.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import dev.olaore.nventry.R


class ProductImagesAdapter(
    private val ctx: Context,
    val images: List<String>
) : PagerAdapter() {

    override fun getCount() = images.size

    val views = mutableListOf<ImageView>()

    @SuppressLint("SetTextI18n")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val layoutInflater = LayoutInflater.from(ctx)
        val productImageView = layoutInflater.inflate(R.layout.product_image, container, false)
        val productImage: ImageView = productImageView.findViewById(R.id.product_image_view)

        Glide.with(ctx).load(images[position]).placeholder(R.drawable.no_image).into(productImage)
        container.addView(productImageView)

        views.add(productImage)

        return productImageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (view == (`object` as ConstraintLayout))
    }

}