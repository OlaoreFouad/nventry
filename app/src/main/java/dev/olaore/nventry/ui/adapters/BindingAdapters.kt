package dev.olaore.nventry.ui.adapters

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import dev.olaore.nventry.R

@BindingAdapter("indicatorWidth")
fun setIndicatorStyle(view: TextView, state: Boolean) {

    val layoutParams = view.layoutParams
    layoutParams.width = if (state) {
        view.resources.getDimension(R.dimen.active_indicator_width).toInt()
    } else {
        view.resources.getDimension(R.dimen.indicator_size).toInt()
    }
    view.layoutParams = layoutParams

}

@SuppressLint("SetTextI18n")
@BindingAdapter("displayPrice")
fun setPrice(view: TextView, price: Float) {
    view.text = "AED $price"
}

@BindingAdapter("displayPlainPrice")
fun removePrice(view: TextView, stringedPrice: Any) {
    view.text = stringedPrice.toString().replace("AED", "").trim()
}

@SuppressLint("SetTextI18n")
@BindingAdapter("displayQuantity")
fun setQuantity(view: TextView, qty: Int) {
    view.text = "$qty"
}

@BindingAdapter("errorText")
fun errorText(view: TextInputLayout, text: String) {
    view.error = text
}

@BindingAdapter("extErrorText")
fun extErrorText(view: TextInputLayout, text: String) {
    if (view.editText!!.text.isNotBlank()) {
        view.error = text
    }
}